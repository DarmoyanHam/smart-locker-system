package com.lockerdevice.myapplication;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.List;

public class Rs485Controller {

    private static final String TAG = "Rs485Controller";
    private static final String ACTION_USB_PERMISSION = "com.example.smartlocker.USB_PERMISSION";

    private UsbManager usbManager;
    private UsbSerialPort serialPort;
    private Context context;

    private PendingIntent permissionIntent;
    private boolean permissionGranted = false;

    public Rs485Controller(Context context) {
        this.context = context;
        usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION),
                    PendingIntent.FLAG_IMMUTABLE);
        } else {
            permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_IMMUTABLE);
        }

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        ContextCompat.registerReceiver(context, usbReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED);

        openUsbSerialPort();
    }

    private void openUsbSerialPort() {
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);

        if (availableDrivers.isEmpty()) {
            Log.e(TAG, "No USB serial drivers found");
            return;
        }

        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDevice device = driver.getDevice();

        if (!usbManager.hasPermission(device)) {
            usbManager.requestPermission(device, permissionIntent);
            Log.i(TAG, "Requested USB permission");
            return;
        }

        try {
            serialPort = driver.getPorts().get(0);
            serialPort.open(usbManager.openDevice(device));
            serialPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            Log.i(TAG, "Serial port opened");
            permissionGranted = true;
        } catch (IOException e) {
            Log.e(TAG, "Error opening serial port", e);
        }
    }

    public void close() {
        try {
            if (serialPort != null) {
                serialPort.close();
                serialPort = null;
            }
            context.unregisterReceiver(usbReceiver);
        } catch (IOException e) {
            Log.e(TAG, "Error closing serial port", e);
        }
    }

    /**
     * Формирует команду открытия/закрытия бокса.
     * Пример протокола: [0x7E][boxNumber][command][0x0D]
     * command: 0x01 - открыть, 0x00 - закрыть
     */
    public byte[] buildCommand(int boxNumber, boolean open) {
        return new byte[] {
                0x7E,
                (byte) boxNumber,
                (byte) (open ? 0x01 : 0x00),
                0x0D
        };
    }

    public void sendToSerialPort(byte[] command) {
        if (serialPort == null || !permissionGranted) {
            Log.e(TAG, "Serial port not open or permission not granted");
            return;
        }

        try {
            serialPort.write(command, 1000);
            Log.i(TAG, "Sent command: " + bytesToHex(command));
        } catch (IOException e) {
            Log.e(TAG, "Error sending command", e);
        }
    }

    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_USB_PERMISSION.equals(intent.getAction())) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            Log.i(TAG, "USB permission granted");
                            openUsbSerialPort();
                        }
                    } else {
                        Log.e(TAG, "USB permission denied");
                    }
                }
            }
        }
    };

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
