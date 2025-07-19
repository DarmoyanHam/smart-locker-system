import { Button } from "antd";
import { HOME_PATH } from "../consts/paths";
import { useNavigate } from "react-router-dom";
import { LeftOutlined } from "@ant-design/icons";
import "./BackButton.css";

export const BackButton = ({ children }) => {
    const navigate = useNavigate();

    return <>
        <Button onClick={() => navigate(HOME_PATH)} className="back-button-wrapper" >{<LeftOutlined />}Back</Button>
        {children}
    </>
}