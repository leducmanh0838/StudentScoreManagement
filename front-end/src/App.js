import './App.css';
import { useReducer } from 'react';
import MyUserReducer from './reducers/MyUserReducer';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Container } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './components/layout/Header';
import Signup from './components/Auth/Signup';
import VerifyOTP from './components/Auth/VerifyOTP';
import Login from './components/Auth/Login';
import CourseSessionList from './components/Course/CourseSessionList';
import { MyDispatchContext, MyUserContext } from './configs/Contexts';
import cookie from 'react-cookies'
import EnrollmentListByStudent from './components/Course/EnrollmentListByStudent';
import TeacherCourseSessions from './components/Course/TeacherCourseSessions ';
import CourseSessionStudents from './components/Course/CourseSessionStudents ';
import GradeManagement from './components/Grade/GradeManagement';

const initUser = () => {
  const cookieUser = cookie.load('user');
  return cookieUser ? cookieUser : null;
}

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null, initUser);

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatchContext.Provider value={dispatch}>
        <BrowserRouter>
          <Header />

          <Routes>
            {/* <Route path="/" element={<Home />} /> */}
            <Route path="/signup" element={<Signup />} />
            <Route path="/verify-otp" element={<VerifyOTP />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register-course-session" element={<CourseSessionList />} />
            <Route path="/enrollment-list" element={<EnrollmentListByStudent />} />

            <Route path="/teacher">
              <Route path="course-sessions" element={<TeacherCourseSessions />} />
              <Route path="course-sessions/:courseSessionId/students" element={<CourseSessionStudents />} />
              <Route path="course-sessions/:courseSessionId/grades" element={<GradeManagement />} />

            </Route>
          </Routes>

          {/* <Footer /> */}
        </BrowserRouter>
      </MyDispatchContext.Provider>
    </MyUserContext.Provider>

  )
}

export default App;
