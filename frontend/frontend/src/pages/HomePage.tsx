import {Typography, Button, Space} from "antd";
import { useNavigate } from "react-router-dom";
import { PUT_PATH, TAKE_PATH } from "../consts/paths";

const {Title} = Typography;

export const HomeContainer = () => {
    const navigate = useNavigate();

    return (
        <div>
            <Title level={3}>Are you going to put or take something?</Title>
            <Space>
                <Button onClick={() => navigate(TAKE_PATH)}>Pick up</Button>
                <Button type="primary" onClick={() => navigate(PUT_PATH)}>Put</Button>
            </Space>
        </div>
    )
}