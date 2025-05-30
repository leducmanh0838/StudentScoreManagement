// // import { useEffect, useContext } from 'react';
// // import { collection, onSnapshot, query, orderBy } from 'firebase/firestore';
// // import { db } from '../../firebase/firebase';
// // import { ChatContext, MyUserContext } from '../../configs/Contexts';

// // const UnreadCountListener = () => {
// //   const { currentChatUser, setUnreadCount } = useContext(ChatContext);
// //   const user = useContext(MyUserContext);

// //   useEffect(() => {
// //     if (!user?.userId) return;

// //     const unsubscribe = onSnapshot(collection(db, 'chats'), (chatRoomSnap) => {
// //       let totalUnread = 0;

// //       chatRoomSnap.docs.forEach((chatRoomDoc) => {
// //         const chatRoomId = chatRoomDoc.id;
// //         if (!chatRoomId.includes(user.userId)) return;

// //         const messagesQuery = query(
// //           collection(db, 'chats', chatRoomId, 'messages'),
// //           orderBy('timestamp')
// //         );

// //         onSnapshot(messagesQuery, (msgSnap) => {
// //           msgSnap.docs.forEach((doc) => {
// //             const data = doc.data();
// //             const readBy = data.readBy || [];
// //             const senderId = data.senderId;

// //             // Chỉ tính tin nhắn chưa đọc và không phải của mình
// //             if (!readBy.includes(user.userId) && senderId !== user.userId) {
// //               totalUnread += 1;
// //             }
// //           });

// //           setUnreadCount(totalUnread);
// //         });
// //       });
// //     });

// //     return () => unsubscribe();
// //   }, [user?.userId]);

// //   return null; // Không render gì cả
// // };

// // export default UnreadCountListener;


// import { useContext, useEffect, useRef } from 'react';
// import { ChatContext, MyUserContext } from '../../configs/Contexts';
// import { db } from '../../firebase/firebase';
// import { collection, onSnapshot, query, orderBy } from 'firebase/firestore';

// const UnreadCountListener = () => {
//   const { setUnreadCount } = useContext(ChatContext);
//   const user = useContext(MyUserContext);
//   const messageUnsubscribers = useRef([]);

//   useEffect(() => {
//     if (!user?.userId) return;

//     console.log("🔔 Starting UnreadCountListener for user:", user.userId);

//     // Cleanup tất cả old message snapshot
//     messageUnsubscribers.current.forEach(unsub => unsub());
//     messageUnsubscribers.current = [];

//     const unsubscribeChats = onSnapshot(collection(db, 'chats'), (chatRoomSnap) => {
//       const relevantRooms = chatRoomSnap.docs.filter(doc => doc.id.includes(user.userId));

//       console.log("📦 Found chat rooms:", relevantRooms.map(d => d.id));

//       // Cleanup old message snapshot listeners
//       messageUnsubscribers.current.forEach(unsub => unsub());
//       messageUnsubscribers.current = [];

//       let totalUnread = 0;

//       relevantRooms.forEach((chatRoomDoc) => {
//         const chatRoomId = chatRoomDoc.id;

//         const messagesQuery = query(
//           collection(db, 'chats', chatRoomId, 'messages'),
//           orderBy('timestamp')
//         );

//         const unsubscribeMessages = onSnapshot(messagesQuery, (msgSnap) => {
//           let unreadInRoom = 0;

//           msgSnap.docs.forEach((doc) => {
//             const data = doc.data();
//             const readBy = data.readBy || [];
//             const senderId = data.senderId;

//             if (!readBy.includes(user.userId) && senderId !== user.userId) {
//               unreadInRoom += 1;
//             }
//           });

//           totalUnread = 0;

//           // Tổng lại toàn bộ unread từ các chatRoom đang theo dõi
//           messageUnsubscribers.current.forEach((_, idx) => {
//             totalUnread += unreadInRoom;
//           });

//           console.log(`📨 Unread in room ${chatRoomId}:`, unreadInRoom);
//           console.log(`🔢 Total unread (across rooms):`, totalUnread);

//           setUnreadCount(totalUnread);
//         });

//         messageUnsubscribers.current.push(unsubscribeMessages);
//       });
//     });

//     return () => {
//       console.log("🧹 Cleaning up UnreadCountListener");
//       unsubscribeChats();
//       messageUnsubscribers.current.forEach(unsub => unsub());
//     };
//   }, [user?.userId]);

//   return null;
// };

// export default UnreadCountListener;

import { useContext, useEffect } from 'react';
import { ChatContext, MyUserContext } from '../../configs/Contexts';
import { db } from '../../firebase/firebase';
import { collectionGroup, onSnapshot, query, where } from 'firebase/firestore';

const UnreadCountListener = () => {
  const { setUnreadCount } = useContext(ChatContext);
  const user = useContext(MyUserContext);

  useEffect(() => {
    // if (!user?.userId) return;

    // // Lắng nghe tất cả tin nhắn chưa đọc gửi đến user
    // const q = query(
    //   collectionGroup(db, 'messages'),
    //   where('readBy', 'not-in', [[user.userId]])      // không phải tin nhắn do user gửi
    // );

    // const unsubscribe = onSnapshot(q, (snapshot) => {
    //   const unread = snapshot.docs.filter(doc => doc.data().senderId !== user.userId);
    //   // Số lượng tin nhắn chưa đọc là số document snapshot trả về
    //   setUnreadCount(snapshot.docs.length);
    // });

    // return () => unsubscribe();
  }, [user?.userId, setUnreadCount]);

  return null;
};

export default UnreadCountListener;

