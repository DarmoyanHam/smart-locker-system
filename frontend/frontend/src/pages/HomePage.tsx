import {Typography, Button, Space} from "antd";
import { useNavigate } from "react-router-dom";
import { ADMIN_PATH, CLIENT_PATH } from "../consts/paths";

const {Title} = Typography;

export const HomeContainer = () => {
    const navigate = useNavigate();

    return (
        <div>
            <Title level={3}>Are you admin or client?</Title>
            <Space>
                <Button onClick={() => navigate(ADMIN_PATH)}>Admin</Button>
                <Button type="primary" onClick={() => navigate(CLIENT_PATH)}>Client</Button>
            </Space>
        </div>
    )
}