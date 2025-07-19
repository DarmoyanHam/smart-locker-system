import React, { createContext, useContext, useState } from "react";
import type { ReactNode } from "react";

type QrContextType = {
  qrImage: string | null;
  setQrImage: (image: string | null) => void;
};

const QrContext = createContext<QrContextType | undefined>(undefined);

export const QrProvider = ({ children }: { children: ReactNode }) => {
  const [qrImage, setQrImage] = useState<string | null>(null);

  return (
    <QrContext.Provider value={{ qrImage, setQrImage }}>
      {children}
    </QrContext.Provider>
  );
};

export const useQrContext = (): QrContextType => {
  const context = useContext(QrContext);
  if (!context) {
    throw new Error("useQrContext must be used within a QrProvider");
  }
  return context;
};
