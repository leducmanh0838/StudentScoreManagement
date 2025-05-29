import React, { useContext, useEffect, useState } from 'react';
import { ChatContext, MessengerUIContext, MyUserContext } from '../../configs/Contexts';
import axios from 'axios';
import { UserRoles } from '../../configs/MyValue';
import { authApis, endpoints } from '../../configs/Apis';

const FriendsList = () => {
  const { setCurrentChatUser } = useContext(ChatContext);
  const { setShowFriendList } = useContext(MessengerUIContext);
  const user = useContext(MyUserContext);

  const [friends, setFriends] = useState([]);
  const [searchName, setSearchName] = useState('');

  // Tính role ngược lại
  const getOppositeRole = (role) => {
    if (!role) return UserRoles.ROLE_TEACHER; // default
    if (role === UserRoles.ROLE_STUDENT) return UserRoles.ROLE_TEACHER;
    if (role === UserRoles.ROLE_TEACHER) return UserRoles.ROLE_STUDENT;
    return UserRoles.ROLE_STUDENT; // fallback
  };

  useEffect(() => {
    if (!user) return;

    const roleToFind = getOppositeRole(user.role);

    // tạo timeout trì hoãn 500ms mới gọi API
    const handler = setTimeout(() => {
      const fetchFriends = async () => {
        try {
          const params = new URLSearchParams();
          if (searchName.trim() !== '') {
            params.append('name', searchName.trim());
          }
          else
            return;
          params.append('role', roleToFind);

          const response = await authApis().get(
            `${endpoints['find-users']}?${params.toString()}`
          );

          setFriends(response.data || []);
        } catch (error) {
          console.error('Lỗi khi lấy danh sách bạn bè:', error);
          setFriends([]);
        }
      };

      fetchFriends();
    }, 500);

    // cleanup function: huỷ timeout nếu searchName thay đổi trong 500ms
    return () => clearTimeout(handler);

  }, [user, searchName]);

  const handleSelectFriend = (friend) => {
    console.info("friend: ", friend)
    setCurrentChatUser(friend);
    // toggleMessenger();
    setShowFriendList(false);
  };

  return (
    <div
      className="position-fixed bottom-0 end-0 m-3"
      style={{
        width: 300,
        maxHeight: 400,
        overflowY: 'auto',
        zIndex: 1060,
        backgroundColor: 'white',
        boxShadow: '0 0 10px rgba(0,0,0,0.3)',
        borderRadius: 8,
      }}
    >
      <div className="p-2 border-bottom bg-primary text-white fw-bold">
        Danh sách bạn bè
      </div>

      <div className="p-2">
        <input
          type="text"
          className="form-control"
          placeholder="Tìm bạn theo tên..."
          value={searchName}
          onChange={(e) => setSearchName(e.target.value)}
        />
      </div>

      <ul className="list-group list-group-flush">
        {friends.length === 0 && <li className="list-group-item">Chưa có bạn bè</li>}
        {friends.map((f) => (
          <li
            key={f.userId}
            className="list-group-item list-group-item-action"
            style={{ cursor: 'pointer' }}
            onClick={() => handleSelectFriend(f)}
          >
            {f.firstName} {f.lastName}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default FriendsList;
