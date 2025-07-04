import { createBrowserRouter } from "react-router-dom";
import { 
    HOME_PATH,
    CLIENT_PATH,
    ADMIN_PATH
} from "../consts/paths";
import { HomeContainer } from "../pages/HomePage";
import { ClientContainer } from "../pages/ClientPage";
import { AdminContainer } from "../pages/AdminPage";
import type { Layout } from "antd";
import { BackButton } from "../pages/BackButton";

export const router = createBrowserRouter([
    {
        path: HOME_PATH,
        element: <HomeContainer />,
    },
    {
        path: CLIENT_PATH,
        element: <BackButton>
            <ClientContainer />
        </BackButton>,
    },
    {
        path: ADMIN_PATH,
        element: <BackButton>
            <AdminContainer />
        </BackButton>,
    }
]);