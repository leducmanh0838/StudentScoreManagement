import { useContext, useEffect } from 'react';
import { collectionGroup, onSnapshot } from 'firebase/firestore';
import { db } from '../../firebase/firebase';
import { MyUserContext } from '../../configs/Contexts';
import { MessengerUIContext } from '../../configs/Contexts';

const UnreadCountListener = () => {
  const user = useContext(MyUserContext);
  const { setUnreadCount, setUnreadUserIds } = useContext(MessengerUIContext);

  useEffect(() => {
    if (!user) return;

    // Lắng nghe tất cả message chưa đọc thuộc về user hiện tại
    const unsubscribe = onSnapshot(
      collectionGroup(db, 'messages'), // Tất cả messages trong tất cả chatRooms
      (snapshot) => {
        let count = 0;

        const unreadUserSet = new Set();

        snapshot.docs.forEach((doc) => {
          const data = doc.data();

          // Điều kiện: không phải do mình gửi + mình chưa đọc
          if (data.receiverId === user.userId && data.senderId !== user.userId && !data.readBy?.includes(user.userId)) {
            count += 1;
            unreadUserSet.add(data.senderId);
          }
        });

        setUnreadCount(count); // Cập nhật số tin nhắn chưa đọc
        // const unreadIds = Array.from(unreadUserSet);
        setUnreadUserIds(Array.from(unreadUserSet));
      }
    );

    return () => unsubscribe(); // Hủy listener khi unmount
  }, [user, setUnreadCount]);

  return null; // Không hiển thị gì cả
};

export default UnreadCountListener;
