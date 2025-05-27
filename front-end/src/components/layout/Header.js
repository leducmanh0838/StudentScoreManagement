import { Container, Nav, Navbar, Button } from "react-bootstrap";
import { NavLink, useNavigate } from "react-router-dom";
import { FaGraduationCap } from "react-icons/fa";
import { useContext } from "react";
import { MyDispatchContext, MyUserContext } from "../../configs/Contexts";
import { UserRoles } from "../../configs/MyValue";

const Header = () => {
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch({ type: 'logout' }); // bạn cần định nghĩa hành động này trong reducer
    navigate('/login');
  };

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
