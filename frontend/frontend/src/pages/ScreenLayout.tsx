import { Card, Layout } from "antd";
import { PropsWithChildren } from "react";
import "./ScreenLayout.css";

export const ScreenLayout = ({ children }: PropsWithChildren) => {
  return (
    <Layout className="fullscreen-layout">
      <Card className="screen-card">
        {children}
      </Card>
    </Layout>
  );
};
