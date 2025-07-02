import { Button, Input, Space, Form, Typography } from "antd";

const { Title, Text } = Typography;

export const AdminContainer = () => {
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
                <Button type="primary">Open</Button>
            </Form.Item>
        </Form>
    )
}