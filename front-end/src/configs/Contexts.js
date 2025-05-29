import { createContext } from "react";

export const MyUserContext = createContext();
export const MyDispatchContext = createContext();
// export const MyCartContext = createContext();

// Context quản lý danh sách bạn bè (friend list)
export const FriendsListContext = createContext();

// Context quản lý chat box, tin nhắn đang mở
export const ChatContext = createContext();

// Context quản lý UI messenger (ví dụ: hiển thị/ẩn danh sách bạn bè)
export const MessengerUIContext = createContext();