import { Container, Nav, Navbar, Button } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import { FaGraduationCap } from "react-icons/fa";

const Header = () => {
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
            <NavLink to="/register-course" className="nav-link">Đăng ký môn học</NavLink>
            <NavLink to="/course-list" className="nav-link">Danh sách môn học</NavLink>
          </Nav>

          {/* Đăng nhập + Đăng ký bên phải, styled như nút */}
          <Nav className="d-flex gap-2">
            <NavLink to="/login">
              <Button variant="outline-light" className="rounded-pill px-4">Đăng nhập</Button>
            </NavLink>
            <NavLink to="/signup">
              <Button variant="warning" className="rounded-pill px-4">Đăng ký</Button>
            </NavLink>
          </Nav>

        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
