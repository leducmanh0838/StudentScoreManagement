// import { useEffect, useState } from 'react';
// import {
//   collectionGroup,
//   onSnapshot,
//   query,
//   where
// } from 'firebase/firestore';
// import { db } from './firebase'; // sửa lại path tuỳ theo dự án bạn

// const useUnreadMessages = (currentUserId) => {
//   const [unreadMessages, setUnreadMessages] = useState([]);

//   useEffect(() => {
//     if (!currentUserId) return;

//     // Lắng nghe tất cả tin nhắn chưa đọc gửi đến currentUserId
//     const q = query(
//       collectionGroup(db, 'messages'), // tìm mọi messages trong mọi chatRoom
//       where('readBy', 'not-in', [[currentUserId]]),
//       where('senderId', '!=', currentUserId) // chỉ lấy tin nhắn của người khác gửi
//     );

//     const unsubscribe = onSnapshot(q, (snapshot) => {
//       const unread = snapshot.docs.map((doc) => ({
//         id: doc.id,
//         ...doc.data(),
//         chatRoomId: doc.ref.parent.parent.id, // truy ngược lại chatRoomId
//       }));
//       setUnreadMessages(unread);
//     });

//     return () => unsubscribe();
//   }, [currentUserId]);

//   return unreadMessages;
// };
