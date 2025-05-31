import React, { useContext, useEffect, useState } from 'react';
import { ChatContext, MessengerUIContext, MyUserContext } from '../../configs/Contexts';
import axios from 'axios';
import { getDefaultAvatar, UserRoles } from '../../configs/MyValue';
import { authApis, endpoints } from '../../configs/Apis';
import { collection, getDocs, query, where } from 'firebase/firestore';
import { db } from '../../firebase/firebase';

const FriendsList = () => {
    const { setCurrentChatUser } = useContext(ChatContext);
    const { setShowFriendList, unreadUserIds } = useContext(MessengerUIContext);
    const user = useContext(MyUserContext);

    const [friends, setFriends] = useState([]);
    const [searchName, setSearchName] = useState('');

    const [unreadFriends, setUnreadFriends] = useState([]);

    const fetchUnreadUsersInfo = async () => {
        console.info('fetchUnreadUsersInfo')
        if (!unreadUserIds || unreadUserIds.length === 0) {
            setUnreadFriends([]);
            return;
        }
        console.info('pass')
        const usersRef = collection(db, 'users');
        const q = query(usersRef, where('userId', 'in', unreadUserIds));

        const snapshot = await getDocs(q);

        const userList = snapshot.docs.map(doc => ({
            id: doc.id,
            ...doc.data()
        }));

        console.info('userList: ', JSON.stringify(userList, null, 2));

        setUnreadFriends(userList);
    };

    const getOppositeRole = (role) => {
        if (!role) return UserRoles.ROLE_TEACHER; 
        if (role === UserRoles.ROLE_STUDENT) return UserRoles.ROLE_TEACHER;
        if (role === UserRoles.ROLE_TEACHER) return UserRoles.ROLE_STUDENT;
        return UserRoles.ROLE_STUDENT; 
    };

    useEffect(() => {
        if (!user) return;


        if (!searchName)
            fetchUnreadUsersInfo();

        const roleToFind = getOppositeRole(user.role);

        // chờ 500ms mới gọi API
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

        // huỷ timeout
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
            className="position-fixed top-0 end-0 d-flex flex-column"
            style={{
                width: 320,
                height: '100vh',
                backgroundColor: 'white',
                boxShadow: '0 0 10px rgba(0,0,0,0.3)',
                borderRadius: '0 0 0 8px',
                zIndex: 1060,
            }}
        >
            <div className="p-3 border-bottom bg-primary text-white fw-bold d-flex justify-content-between align-items-center">
                {/* <span>{unreadUserIds}</span> */}
                <span>Danh sách bạn bè</span>
                <button
                    type="button"
                    onClick={() => setShowFriendList(false)}
                    style={{
                        background: 'transparent',
                        border: 'none',
                        color: 'white',
                        fontSize: '1.5rem',
                        lineHeight: 1,
                        cursor: 'pointer',
                        padding: 0,
                    }}
                    aria-label="Close friend list"
                >
                    &times;
                </button>
            </div>

            <div className="p-3">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Tìm bạn theo tên..."
                    value={searchName}
                    onChange={(e) => setSearchName(e.target.value)}
                />
            </div>

            <ul
                className="list-group list-group-flush flex-grow-1 overflow-auto"
                style={{ minHeight: 0 }}
            >
                {friends.length === 0 && (
                    <>

                        {unreadFriends.length !== 0 ? <>
                            {unreadFriends.map((f) => (
                                <li
                                    key={f.userId}
                                    className="list-group-item list-group-item-action d-flex align-items-center"
                                    style={{ cursor: 'pointer' }}
                                    onClick={() => handleSelectFriend(f)}
                                >
                                    {/* Avatar */}
                                    <img
                                        src={f.avatar || getDefaultAvatar(f.firstName, f.lastName)} 
                                        alt={`${f.firstName} ${f.lastName}`}
                                        style={{
                                            width: 40,
                                            height: 40,
                                            borderRadius: '50%',
                                            objectFit: 'cover',
                                            marginRight: 12,
                                        }}
                                    />

                                    <span>{f.firstName} {f.lastName}</span>

                                    <span className="ms-auto">
                                        <span
                                            className="badge bg-danger"
                                            style={{ fontSize: 10, padding: '6px 8px', borderRadius: '50%' }}
                                            title="Tin nhắn chưa đọc"
                                        >
                                            !
                                        </span>
                                    </span>
                                </li>
                            ))}
                        </> : <>
                            <li className="list-group-item">Chưa có bạn bè</li>
                        </>}
                    </>
                )}
                {friends.map((f) => (
                    <li
                        key={f.userId}
                        className="list-group-item list-group-item-action d-flex align-items-center"
                        style={{ cursor: 'pointer' }}
                        onClick={() => handleSelectFriend(f)}
                    >
                        <img
                            src={f.avatar || getDefaultAvatar(f.firstName, f.lastName)}
                            alt={`${f.firstName} ${f.lastName}`}
                            style={{
                                width: 40,
                                height: 40,
                                borderRadius: '50%',
                                objectFit: 'cover',
                                marginRight: 12,
                            }}
                        />
                        
                        <span>{f.firstName} {f.lastName}</span>
                    </li>
                ))}
            </ul>
        </div>
    );

};

export default FriendsList;
