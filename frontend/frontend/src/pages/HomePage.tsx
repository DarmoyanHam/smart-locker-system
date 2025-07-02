import {Typography, Button, Space} from "antd";

const {Title} = Typography;

export const HomeContainer = () => {
    return (
        <div>
            <Title level={3}>Are you admin or client?</Title>
            <Space>
                <Button>Admin</Button>
                <Button type="primary">Client</Button>
            </Space>
        </div>
    )
}