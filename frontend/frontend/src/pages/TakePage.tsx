import {QrcodeOutlined} from "@ant-design/icons";
import { Button, Space, Typography } from "antd";
import { useNavigate } from "react-router-dom";
import { HOME_PATH } from "../consts/paths";

const { Title } = Typography;

export const TakeContainer = () => {
    const navigate = useNavigate();

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