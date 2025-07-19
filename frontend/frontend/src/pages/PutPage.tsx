import { Button, Input, Space, Form, Typography, Select, message } from "antd";
import { HOME_PATH, QR_PATH } from "../consts/paths";
import { useNavigate } from "react-router-dom";
import { useQrContext } from "./QRContext";

const { Title } = Typography;

export const PutContainer = () => {
    const { setQrImage } = useQrContext();
    const navigate = useNavigate();

    const onFinish = async ({size, time} : {size: string, time: string}) => {
        try {
            const durationInMinutes = parseInt(time) * 60;
            console.log(durationInMinutes);

            const response = await fetch(`http://192.168.18.6:8080/boxes/lock?durationMinute=${durationInMinutes}`, {
                method: "POST",
                headers: { "Content-Type" : "application/json" },
                body: JSON.stringify({ durationInMinutes }),
            });

            if(!response.ok) {
                message.error("Something went wrong.");
                return;
            }

            const blob = await response.blob();
            const qrUrl = URL.createObjectURL(blob);
            setQrImage(qrUrl);
            navigate(QR_PATH);
        } catch(error) {
            console.error(error);
            message.error("Something went wrong in request.");
        }
    };

    return (
        <Form 
            name="Put"
            onFinish={onFinish}
            initialValues={{ time: "1", size: "normal" }}
        >
            <Form.Item name="form-name">
                <Title level={3} >Fill these fields to open an empty box.</Title>
            </Form.Item>
            <Form.Item>

            </Form.Item>
            <Form.Item 
                label="Size"
                name="size"
                rules={[{ required: true, message: 'Please select the size!' }]}
            >
                <Select 
                    options={[
                        {value: "small", label: "Small"},
                        {value: "normal", label: "Normal"},
                        {value: "large", label: "Large"},
                    ]}
                />
            </Form.Item>
            <Form.Item 
                label="Time"
                name="time"
                rules={[{ required: true, message: 'Please select the time(h)!' }]}
            >
                <Select 
                    options={[
                        {value: "1", label: "1"},
                        {value: "2", label: "2"},
                        {value: "3", label: "3"},
                        {value: "5", label: "5"},
                        {value: "12", label: "12"},
                        {value: "24", label: "24"},
                        {value: "other", label: "Other"},
                    ]}
                />
            </Form.Item>
            <Form.Item name="submit">
                <Space>
                    <Button onClick={() => navigate(HOME_PATH)}>Cancel</Button>
                    <Button type="primary" htmlType="submit">Open</Button>
                </Space>
            </Form.Item>
        </Form>
    )
}