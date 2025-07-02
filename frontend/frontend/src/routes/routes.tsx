import { createBrowserRouter } from "react-router-dom";
import { 
    HOME_PATH,
    CLIENT_PATH,
    ADMIN_PATH
} from "../consts/paths";
import { HomeContainer } from "../pages/HomePage";
import { ClientContainer } from "../pages/ClientPage";
import { AdminContainer } from "../pages/AdminPage";

export const router = createBrowserRouter([
    {
        path: HOME_PATH,
        element: <HomeContainer />,
    },
    {
        path: CLIENT_PATH,
        element: <ClientContainer />,
    },
    {
        path: ADMIN_PATH,
        element: <AdminContainer />,
    }
]);