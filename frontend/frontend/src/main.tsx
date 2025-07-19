import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { QrProvider } from './pages/QRContext.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <QrProvider>
      <App />
    </QrProvider>
  </StrictMode>,
)
