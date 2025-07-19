import { createBrowserRouter } from "react-router-dom";
import { 
    HOME_PATH,
    TAKE_PATH,
    PUT_PATH,
    QR_PATH
} from "../consts/paths";
import { HomeContainer } from "../pages/HomePage";
import { TakeContainer } from "../pages/TakePage";
import { PutContainer } from "../pages/PutPage";
import { BackButton } from "../pages/BackButton";
import { ScreenLayout } from "../pages/ScreenLayout";
import { QRDisplay } from "../pages/QRDisplay";

export const router = createBrowserRouter([
    {
        path: HOME_PATH,
        element: <ScreenLayout>
            <HomeContainer />
        </ScreenLayout>,
    },
    {
        path: TAKE_PATH,
        element: <ScreenLayout>
            <BackButton>
                <TakeContainer />
            </BackButton>
        </ScreenLayout>
    },
    {
        path: PUT_PATH,
        element: <ScreenLayout>
            <BackButton>
                <PutContainer />
            </BackButton>
        </ScreenLayout>
    },
    {
        path: QR_PATH,
        element: <ScreenLayout>
            <BackButton>
                <QRDisplay />
            </BackButton>
        </ScreenLayout>
    }
]);