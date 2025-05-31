import { db } from './firebase.js';
import { collection, addDoc, serverTimestamp } from 'firebase/firestore';

const sendMessage = async (chatRoomId, senderId, senderName, message) => {
  const docRef = await addDoc(collection(db, "chats", chatRoomId, "messages"), {
    senderId,
    senderName,
    message,
    timestamp: serverTimestamp()
  });

  console.log("Message sent with ID:", docRef.id);
};

const [chatRoomId, senderId, senderName, ...messageParts] = process.argv.slice(2);
const message = messageParts.join(" ");
sendMessage(chatRoomId, senderId, senderName, message);
