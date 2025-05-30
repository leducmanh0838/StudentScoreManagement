import React, { useEffect, useRef, useState, useContext } from 'react';
import { db } from '../../firebase/firebase';
import {
  collection,
  query,
  orderBy,
  onSnapshot,
  addDoc,
  serverTimestamp,
  updateDoc,
  doc,
  getDoc,
  setDoc
} from 'firebase/firestore';
import { ChatContext, MyUserContext } from '../../configs/Contexts';

const ChatBox = () => {
  const { currentChatUser: activeChatUser, setCurrentChatUser: setActiveChatUser } = useContext(ChatContext);
  const user = useContext(MyUserContext);

  const [messages, setMessages] = useState([]);
  const [newMsg, setNewMsg] = useState('');
  const bottomRef = useRef();

  const chatRoomId = activeChatUser
    ? [user.userId, activeChatUser.userId].sort().join('_')
    : null;

  useEffect(() => {
    if (!chatRoomId) {
      setMessages([]);
      return;
    }

    // Thêm đoạn kiểm tra và khởi tạo user
    const chatDocRef = doc(db, 'users', String(user.userId));
    getDoc(chatDocRef).then((docSnap) => {
      if (!docSnap.exists()) {
        // Tạo metadata participants
        setDoc(chatDocRef, {
          userId: user.userId,
          firstName: user.firstName,
          lastName: user.lastName,
          avatar: user.avatar || null,
          createdAt: serverTimestamp()
        });
      }
    });

    const q = query(
      collection(db, 'chats', chatRoomId, 'messages'),
      orderBy('timestamp')
    );

    const unsubscribe = onSnapshot(q, async (snapshot) => {
      snapshot.docChanges().forEach(change => {
        if (change.type === 'added') {
          const newMsg = {
            id: change.doc.id,
            ...change.doc.data()
          };
          setMessages(prevMessages => [...prevMessages, newMsg]);
        }

      });

      // Cập nhật readBy nếu chưa có
      const unread = snapshot.docs.filter(
        doc => !doc.data().readBy?.includes(user.userId) && doc.data().senderId !== user.userId
      );

      for (const docSnap of unread) {
        const messageRef = doc(db, 'chats', chatRoomId, 'messages', docSnap.id);
        await updateDoc(messageRef, {
          readBy: [...(docSnap.data().readBy || []), user.userId]
        });
      }

      // setMessages(msgs);
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
      receiverId: activeChatUser.userId,
      senderName: `${user.firstName} ${user.lastName}`,
      message: newMsg,
      timestamp: serverTimestamp(),
      readBy: [user.userId]
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