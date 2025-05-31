import React, { useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Card, Container, ListGroup, Button, Spinner, Form, Row, Col } from 'react-bootstrap';
import { endpoints } from '../../configs/Apis';
import PaginatedData from '../../configs/PaginatedData';

const CourseSessionStudents = () => {
  const navigate = useNavigate();
  const { courseSessionId } = useParams();
  const [searchParams, setSearchParams] = useState({ courseSessionId });

  const [filters, setFilters] = useState({ name: '', userCode: '' });


  const {
    data: students,
    loading,
    loadMore,
    refresh,
    hasMore
  } = PaginatedData(
    endpoints['get-students-by-course-session'],
    searchParams,
    true
  );

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const handleSearch = (e) => {
    e.preventDefault();
    setSearchParams({
      courseSessionId,
      name: filters.name,
      userCode: filters.userCode
    });
  };

  return (
    <Container className="mt-4">
      <Card>
        <Card.Header className="bg-success text-white">
          <h5>Sinh viên của lớp học #{courseSessionId}</h5>
        </Card.Header>
        <Card.Body>

          <Form onSubmit={handleSearch} className="mb-4">
            <Row className="g-2">
              <Col md={5}>
                <Form.Control
                  name="name"
                  value={filters.name}
                  onChange={handleInputChange}
                  placeholder="Nhập họ tên"
                />
              </Col>
              <Col md={5}>
                <Form.Control
                  name="userCode"
                  value={filters.userCode}
                  onChange={handleInputChange}
                  placeholder="Nhập mã sinh viên"
                />
              </Col>
              <Col md={2}>
                <Button type="submit" variant="primary" className="w-100">Tìm kiếm</Button>
              </Col>
            </Row>
          </Form>

          {students.length === 0 && !loading && (
            <p className="text-muted text-center">Không có sinh viên nào phù hợp.</p>
          )}

          <ListGroup variant="flush">
            {students.map((student) => (
              <ListGroup.Item key={student.enrollmentId}  className="d-flex justify-content-between align-items-center py-3">
                <div>
                  <strong>{student.lastName} {student.firstName}</strong> - <span className="text-muted">{student.userCode}</span>
                </div>
                <Button
                  variant="outline-primary"
                  size="sm"
                  onClick={() => navigate(`/user/${student.userId}`)}
                >
                  Xem chi tiết
                </Button>
              </ListGroup.Item>
            ))}
          </ListGroup>

          {loading && (
            <div className="text-center my-3">
              <Spinner animation="border" variant="primary" />
            </div>
          )}

          {hasMore && !loading && (
            <div className="text-center mt-3">
              <Button onClick={loadMore} variant="outline-success">Xem thêm</Button>
            </div>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
};

export default CourseSessionStudents;
