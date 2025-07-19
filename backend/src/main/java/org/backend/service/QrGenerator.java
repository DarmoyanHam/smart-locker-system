package org.backend.service;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.UUID;

public class QrGenerator {

    public static String generateQrImage(String boxId, String uploadDir) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int width = 300;
        int height = 300;

        BitMatrix bitMatrix = qrCodeWriter.encode(boxId, BarcodeFormat.QR_CODE, width, height);
        String fileName = "qr_" + UUID.randomUUID() + ".png";
        Path path = FileSystems.getDefault().getPath(uploadDir, fileName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        return path.toString();
    }
}

