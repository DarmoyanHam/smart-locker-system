import { Button } from "antd";
import { HOME_PATH } from "../consts/paths";
import { useNavigate } from "react-router-dom";

export const BackButton = ({ children }) => {
    const navigate = useNavigate();

    return <>
        <Button onClick={() => navigate(HOME_PATH)}>Back</Button>
        {children}
    </>
}