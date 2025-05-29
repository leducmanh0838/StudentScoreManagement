import './App.css';
import { useReducer, useState } from 'react';
import MyUserReducer from './reducers/MyUserReducer';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Container } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './components/layout/Header';
import Signup from './components/Auth/Signup';
import VerifyOTP from './components/Auth/VerifyOTP';
import Login from './components/Auth/Login';
import ChatBox from './components/Chat/ChatBox';
import CourseSessionList from './components/Course/CourseSessionList';
import { ChatContext, FriendsListContext, MessengerUIContext, MyDispatchContext, MyUserContext } from './configs/Contexts';
import cookie from 'react-cookies'
import EnrollmentListByStudent from './components/Course/EnrollmentListByStudent';
import TeacherCourseSessions from './components/Course/TeacherCourseSessions ';
import CourseSessionStudents from './components/Course/CourseSessionStudents ';
import GradeManagement from './components/Grade/GradeManagement';
import CriteriaManagement from './components/Grade/CriteriaManagement';
import ForumPostManagement from './components/ForumPost/ForumPostManagement ';
import ProtectedRoute from './components/layout/ProtectedRoute';
import { UserRoles } from './configs/MyValue';
import FriendsList from './components/Chat/FriendsList';

const initUser = () => {
  const cookieUser = cookie.load('user');
  return cookieUser ? cookieUser : null;
}

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null, initUser);

  // State quản lý danh sách bạn bè
  const [friendsList, setFriendsList] = useState([]);

  // State quản lý tin nhắn và bạn chat hiện tại
  const [currentChatUser, setCurrentChatUser] = useState(null); //userId đang chat
  const [chatMessages, setChatMessages] = useState([]); // messages với bạn chat hiện tại

  // State quản lý UI Messenger: có hiển thị danh sách bạn bè hay không
  const [showFriendList, setShowFriendList] = useState(false);

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatchContext.Provider value={dispatch}>
        <FriendsListContext.Provider value={{ friendsList, setFriendsList }}>
          <ChatContext.Provider value={{ currentChatUser, setCurrentChatUser, chatMessages, setChatMessages }}>
            <MessengerUIContext.Provider value={{ showFriendList, setShowFriendList }}>
              <BrowserRouter>
                <Header />

                <Routes>
                  {/* <Route path="/" element={<Home />} /> */}
                  <Route path="/signup" element={<Signup />} />
                  <Route path="/verify-otp" element={<VerifyOTP />} />
                  <Route path="/login" element={<Login />} />
                  <Route path="/register-course-session" element={<CourseSessionList />} />
                  <Route path="/enrollment-list" element={<EnrollmentListByStudent />} />
                  <Route path="course-sessions/:courseSessionId/forum-posts" element={<ForumPostManagement />} />

                  {/* Các route dành riêng cho TEACHER */}
                  <Route element={<ProtectedRoute allowedRoles={[UserRoles.ROLE_TEACHER]} />}>
                    <Route path="/teacher">
                      <Route path="course-sessions" element={<TeacherCourseSessions />} />
                      <Route path="course-sessions/:courseSessionId/students" element={<CourseSessionStudents />} />
                      <Route path="course-sessions/:courseSessionId/grades" element={<GradeManagement />} />
                      <Route path="course-sessions/:courseSessionId/criterias" element={<CriteriaManagement />} />
                      <Route path="course-sessions/:courseSessionId/forum-posts" element={<ForumPostManagement />} />
                    </Route>
                  </Route>
                </Routes>
                {/* <Footer /> */}
                <ChatBox />
                {/* <FriendsList /> */}
                {showFriendList && <FriendsList />}
              </BrowserRouter>
            </MessengerUIContext.Provider>
          </ChatContext.Provider>
        </FriendsListContext.Provider>
      </MyDispatchContext.Provider>
    </MyUserContext.Provider>

  )
}

export default App;
