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

    // Lắng nghe tất cả message chưa đọc thuộc về user
    const unsubscribe = onSnapshot(
      collectionGroup(db, 'messages'), 
      (snapshot) => {
        let count = 0;

        const unreadUserSet = new Set();

        snapshot.docs.forEach((doc) => {
          const data = doc.data();

          if (data.receiverId === user.userId && data.senderId !== user.userId && !data.readBy?.includes(user.userId)) {
            count += 1;
            unreadUserSet.add(data.senderId);
          }
        });

        setUnreadCount(count); 
        // const unreadIds = Array.from(unreadUserSet);
        setUnreadUserIds(Array.from(unreadUserSet));
      }
    );

    return () => unsubscribe();
  }, [user, setUnreadCount]);

  return null; 
};

export default UnreadCountListener;
