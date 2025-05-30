import { Container, Nav, Navbar, Button } from "react-bootstrap";
import { NavLink, useNavigate } from "react-router-dom";
import { FaFacebookMessenger, FaGraduationCap } from "react-icons/fa";
import { useContext } from "react";
import { ChatContext, MessengerUIContext, MyDispatchContext, MyUserContext } from "../../configs/Contexts";
import { UserRoles } from "../../configs/MyValue";

const Header = () => {
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch({ type: 'logout' }); // bạn cần định nghĩa hành động này trong reducer
    navigate('/login');
  };

  // const { toggleMessenger } = useContext(ChatContext);
  const { showFriendList, setShowFriendList, unreadCount } = useContext(MessengerUIContext);
  const handleMessage = () => {
    setShowFriendList(!showFriendList);
  }

  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="px-3">
      <Container fluid>
        {/* Logo */}
        <Navbar.Brand as={NavLink} to="/" className="d-flex align-items-center">
          <FaGraduationCap className="me-2" />
          Hệ thống điểm Sinh Viên
        </Navbar.Brand>

        {/* Toggle cho mobile */}
        <Navbar.Toggle />
        <Navbar.Collapse>

          {/* Menu trái */}
          <Nav className="me-auto">
            <NavLink to="/" end className="nav-link">Trang chủ</NavLink>
            {user && user.role === UserRoles.ROLE_STUDENT && (
              <>
                <NavLink to="/register-course-session" className="nav-link">Đăng ký môn học</NavLink>
                <NavLink to="/enrollment-list" className="nav-link">Danh sách môn học đã học</NavLink>
              </>
            )}

            {user && user.role === UserRoles.ROLE_TEACHER && (
              <>
                <NavLink to="/teacher/course-sessions" className="nav-link">Danh sách môn học phụ trách</NavLink>
              </>
            )}

          </Nav>

          {/* Đăng nhập + Đăng ký bên phải, styled như nút */}
          <Nav className="d-flex gap-2 align-items-center">
            {user && (
              <button
                onClick={handleMessage}
                className="btn btn-outline-light rounded-circle d-flex justify-content-center align-items-center position-relative"
                style={{ width: 44, height: 44 }}
                title="Messenger"
              >
                <FaFacebookMessenger size={32} />
                {unreadCount > 0 && (
                  <span
                    style={{
                      position: 'absolute',
                      top: 4,
                      right: 4,
                      backgroundColor: 'red',
                      color: 'white',
                      borderRadius: '50%',
                      width: 18,
                      height: 18,
                      fontSize: 12,
                      fontWeight: 'bold',
                      display: 'flex',
                      justifyContent: 'center',
                      alignItems: 'center',
                      userSelect: 'none',
                    }}
                  >
                    {unreadCount > 99 ? '99+' : unreadCount}
                  </span>
                )}
              </button>
            )}
            {user ? (<>
              <div className="flex items-center gap-2">
                <img
                  src={user.avatar || "/avatar-default.jpg"}
                  alt="avatar"
                  style={{ width: "32px", height: "32px", objectFit: "cover", borderRadius: "50%" }}
                />
                <span className="text-white fw-medium text-capitalize ms-2">{user.firstName} {user.lastName}</span>
              </div>
              <button
                onClick={handleLogout}
                className="btn btn-danger ms-2"
              >
                Đăng xuất
              </button>
            </>) : (<>
              <NavLink to="/login">
                <Button variant="outline-light" className="rounded-pill px-4">Đăng nhập</Button>
              </NavLink>
              <NavLink to="/signup">
                <Button variant="warning" className="rounded-pill px-4">Đăng ký</Button>
              </NavLink>
            </>)}

          </Nav>

        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
