import {QrcodeOutlined} from "@ant-design/icons";
import { Button, Space, Typography } from "antd";

const { Title } = Typography;

export const ClientContainer = () => {
    return (
        <div>
            <Title level={4}>Open with</Title>
            <Space>
                <Button icon={<QrcodeOutlined />} type="primary">QR</Button>
                <Button>code</Button>
            </Space>
        </div>
    )
}