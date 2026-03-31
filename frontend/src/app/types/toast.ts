import { ToastType } from "./toastType";

export interface Toast {
  id: string;
  type: ToastType;
  title: string;
  message?: string;
  duration?: number; // ms, default 4000. Pass 0 for persistent
}