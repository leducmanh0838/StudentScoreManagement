import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { FaChalkboardTeacher, FaBookOpen, FaGraduationCap } from 'react-icons/fa';

const Home = () => {
  return (
    <div
      style={{
        backgroundImage: 'url("/home.jpg")',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        minHeight: '100vh',
        position: 'relative',
        color: 'white',
      }}
    >
      <div
        style={{
          position: 'absolute',
          top: 0, left: 0, right: 0, bottom: 0,
          backgroundColor: 'rgba(0, 0, 0, 0.6)',
          zIndex: 1,
        }}
      />

      <Container style={{ position: 'relative', zIndex: 2, paddingTop: '100px' }}>
        <Row className="justify-content-center text-center">
          <Col lg={10}>
            <h1 className="display-4 fw-bold">Hệ Thống Quản Lý Điểm Sinh Viên</h1>
            <p className="lead mt-4">
              Nền tảng hiện đại giúp giảng viên quản lý điểm và sinh viên theo dõi kết quả học tập một cách nhanh chóng và tiện lợi.
            </p>
          </Col>
        </Row>

        <Row className="mt-5 text-center">
          <Col md={4} className="mb-4">
            <Card bg="dark" text="white" className="h-100 shadow-lg border-0">
              <Card.Body>
                <FaGraduationCap size={48} className="mb-3 text-warning" />
                <Card.Title>Theo dõi kết quả học tập</Card.Title>
                <Card.Text>
                  Sinh viên có thể xem điểm số chi tiết theo từng tiêu chí và môn học.
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4} className="mb-4">
            <Card bg="dark" text="white" className="h-100 shadow-lg border-0">
              <Card.Body>
                <FaChalkboardTeacher size={48} className="mb-3 text-info" />
                <Card.Title>Quản lý giảng dạy</Card.Title>
                <Card.Text>
                  Giảng viên dễ dàng nhập, cập nhật và theo dõi điểm của từng sinh viên trong từng học phần.
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
          <Col md={4} className="mb-4">
            <Card bg="dark" text="white" className="h-100 shadow-lg border-0">
              <Card.Body>
                <FaBookOpen size={48} className="mb-3 text-success" />
                <Card.Title>Đăng ký & Diễn đàn</Card.Title>
                <Card.Text>
                  Hệ thống hỗ trợ sinh viên đăng ký học phần và tham gia diễn đàn trao đổi học tập.
                </Card.Text>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      </Container>
    </div>
  );
};

export default Home;
