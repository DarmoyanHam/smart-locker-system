import { useQrContext } from './QRContext';
import { Typography } from 'antd';

const { Title } = Typography;

export const QRDisplay = () => {
    
    const { qrImage } = useQrContext();

    return (
    <>
        { qrImage ? 
            <div style={{ padding: 24, textAlign: 'center' }}>
                <Title level={2}>Your QR code</Title>
                <img src={qrImage} alt='QR code'/>
            </div>
            : <Title level={2}>Not found QR.</Title>
        }
    </>
    )
}