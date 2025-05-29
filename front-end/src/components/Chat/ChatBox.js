// import React, { useEffect, useRef, useState, useContext } from 'react';
// import { db } from '../../firebase/firebase'; // Đã setup Firebase
// import {
//   collection,
//   query,
//   orderBy,
//   onSnapshot,
//   addDoc,
//   serverTimestamp,
// } from 'firebase/firestore';
// import './ChatBox.css';
// import { MyUserContext } from '../../configs/Contexts';

// const ChatBox = () => {
//   const [messages, setMessages] = useState([]);
//   const [newMsg, setNewMsg] = useState('');
//   const [isOpen, setIsOpen] = useState(true); // state ẩn hiện chatbox
//   const user = useContext(MyUserContext);
//   const bottomRef = useRef();

//   const chatRoomId = 'global_chat';

//   useEffect(() => {
//     const q = query(
//       collection(db, 'chats', chatRoomId, 'messages'),
//       orderBy('timestamp')
//     );
//     const unsubscribe = onSnapshot(q, (snapshot) => {
//       const msgs = snapshot.docs.map((doc) => ({
//         id: doc.id,
//         ...doc.data(),
//       }));
//       setMessages(msgs);
//       bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
//     });

//     return () => unsubscribe();
//   }, []);

//   const sendMessage = async (e) => {
//     e.preventDefault();
//     if (!newMsg.trim()) return;

//     await addDoc(collection(db, 'chats', chatRoomId, 'messages'), {
//       senderId: user?.userId || 'anonymous',
//       senderName: user ? `${user.firstName} ${user.lastName}` : 'Khách',
//       message: newMsg,
//       timestamp: serverTimestamp(),
//     });
//     setNewMsg('');
//   };

//   return (
//     <div
//       className="position-fixed bottom-0 end-0 m-3"
//       style={{ width: '320px', zIndex: 1050 }}
//     >
//       {isOpen ? (
//         <div className="card shadow">
//           <div className="card-header bg-primary text-white py-2 px-3 d-flex justify-content-between align-items-center">
//             <span>💬 Hỏi đáp</span>
//             <button
//               className="btn btn-sm btn-light"
//               onClick={() => setIsOpen(false)}
//               aria-label="Đóng chat"
//               title="Ẩn chat"
//             >
//               &times;
//             </button>
//           </div>
//           <div
//             className="card-body p-2"
//             style={{ maxHeight: '300px', overflowY: 'auto' }}
//           >
//             {messages.map((msg) => (
//               <div key={msg.id} className="mb-2">
//                 <strong>{msg.senderName}:</strong> {msg.message}
//               </div>
//             ))}
//             <div ref={bottomRef} />
//           </div>
//           <form onSubmit={sendMessage} className="card-footer p-2 bg-light">
//             <div className="input-group">
//               <input
//                 type="text"
//                 className="form-control"
//                 placeholder="Nhập tin nhắn..."
//                 value={newMsg}
//                 onChange={(e) => setNewMsg(e.target.value)}
//               />
//               <button type="submit" className="btn btn-primary">
//                 Gửi
//               </button>
//             </div>
//           </form>
//         </div>
//       ) : (
//         // Nút nhỏ hiển thị khi chat box bị ẩn
//         <button
//           className="btn btn-primary rounded-circle"
//           style={{ width: '50px', height: '50px' }}
//           onClick={() => setIsOpen(true)}
//           aria-label="Mở chat"
//           title="Hiện chat"
//         >
//           💬
//         </button>
//       )}
//     </div>
//   );
// };

// export default ChatBox;


import React, { useEffect, useRef, useState, useContext } from 'react';
import { db } from '../../firebase/firebase';
import {
  collection,
  query,
  orderBy,
  onSnapshot,
  addDoc,
  serverTimestamp,
  where,
} from 'firebase/firestore';
import { ChatContext } from '../../contexts/ChatContext';
import { MyUserContext } from '../../configs/Contexts';

const ChatBox = () => {
  const { activeChatUser, setActiveChatUser } = useContext(ChatContext);
  const user = useContext(MyUserContext);

  const [messages, setMessages] = useState([]);
  const [newMsg, setNewMsg] = useState('');
  const bottomRef = useRef();

  // Tạo chatRoomId theo 2 user (sắp xếp để luôn đồng nhất)
  const chatRoomId = activeChatUser
    ? [user.userId, activeChatUser.userId].sort().join('_')
    : null;

  useEffect(() => {
    if (!chatRoomId) {
      setMessages([]);
      return;
    }

    const q = query(
      collection(db, 'chats', chatRoomId, 'messages'),
      orderBy('timestamp')
    );

    const unsubscribe = onSnapshot(q, (snapshot) => {
      const msgs = snapshot.docs.map((doc) => ({
        id: doc.id,
        ...doc.data(),
      }));
      setMessages(msgs);
      bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
    });

    return () => unsubscribe();
  }, [chatRoomId]);

  const sendMessage = async (e) => {
    e.preventDefault();
    if (!newMsg.trim()) return;
    if (!chatRoomId) return;

    await addDoc(collection(db, 'chats', chatRoomId, 'messages'), {
      senderId: user.userId,
      senderName: `${user.firstName} ${user.lastName}`,
      message: newMsg,
      timestamp: serverTimestamp(),
    });
    setNewMsg('');
  };

  if (!activeChatUser) return null;

  return (
    <div
      className="position-fixed bottom-0 end-0 m-3"
      style={{ width: 320, zIndex: 1050 }}
    >
      <div className="card shadow">
        <div className="card-header bg-primary text-white d-flex justify-content-between align-items-center py-2 px-3">
          <span>Chat với {activeChatUser.firstName} {activeChatUser.lastName}</span>
          <button
            className="btn btn-sm btn-light"
            onClick={() => setActiveChatUser(null)}
            title="Đóng chat"
          >
            ✖
          </button>
        </div>
        <div
          className="card-body p-2"
          style={{ maxHeight: '300px', overflowY: 'auto' }}
        >
          {messages.map((msg) => (
            <div key={msg.id} className="mb-2">
              <strong>{msg.senderName}:</strong> {msg.message}
            </div>
          ))}
          <div ref={bottomRef} />
        </div>
        <form onSubmit={sendMessage} className="card-footer p-2 bg-light">
          <div className="input-group">
            <input
              type="text"
              className="form-control"
              placeholder="Nhập tin nhắn..."
              value={newMsg}
              onChange={(e) => setNewMsg(e.target.value)}
            />
            <button type="submit" className="btn btn-primary">
              Gửi
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ChatBox;
