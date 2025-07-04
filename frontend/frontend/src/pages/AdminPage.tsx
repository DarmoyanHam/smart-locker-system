import { Button, Input, Space, Form, Typography } from "antd";
import { HOME_PATH } from "../consts/paths";
import { useNavigate } from "react-router-dom";

const { Text } = Typography;

export const AdminContainer = () => {
    const navigate = useNavigate();

    return (
        <Form name="admin">
            <Form.Item name="form-name">
                <Text >Enter your special password to open an empty box.</Text>
            </Form.Item>
            <Form.Item 
                label="Password"
                name="password"
                rules={[{ required: true, message: 'Please input your password!' }]}
            >
                <Input.Password type="text" placeholder="Enter your password" />
            </Form.Item>
            <Form.Item name="submit">
                <Space>
                    <Button onClick={() => navigate(HOME_PATH)}>Cancel</Button>
                    <Button type="primary">Open</Button>
                </Space>
            </Form.Item>
        </Form>
    )
}