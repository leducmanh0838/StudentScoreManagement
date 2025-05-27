import React, { useEffect, useState } from 'react';
import { Spinner, Alert, Table, Container, Card, Button, ButtonGroup } from 'react-bootstrap';
import axios from 'axios';
import { authApis, endpoints } from '../../configs/Apis';
import { useNavigate } from 'react-router-dom';
import { PeopleFill, JournalCheck, ChatDotsFill } from 'react-bootstrap-icons';

const TeacherCourseSessions = () => {
    const [courseSessions, setCourseSessions] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCourseSessions = async () => {
            try {
                setLoading(true);
                setError(null);
                const response = await authApis().get(endpoints['course-sessions-by-teacher']);
                setCourseSessions(response.data);
            } catch (err) {
                setError('Không thể tải danh sách môn học');
            } finally {
                setLoading(false);
            }
        };

        fetchCourseSessions();
    }, []);

    // Các hàm xử lý nút bấm
    const handleViewStudents = (courseId) => {
        navigate(`/teacher/course-sessions/${courseId}/students`);
    };

    const handleManageGrades = (courseId) => {
        navigate(`/teacher/course-sessions/${courseId}/grades`);
    };

    const handleManageForum = (courseId) => {
        navigate(`/teacher/course-sessions/${courseId}/forum`);
    };

    return (
        <Container className="mt-4">
            <Card>
                <Card.Header className="bg-primary text-white">
                    <h4>Danh sách môn học phụ trách</h4>
                </Card.Header>
                <Card.Body>
                    {loading ? (
                        <div className="text-center">
                            <Spinner animation="border" variant="primary" />
                            <p>Đang tải dữ liệu...</p>
                        </div>
                    ) : error ? (
                        <Alert variant="danger" className="text-center">{error}</Alert>
                    ) : courseSessions.length === 0 ? (
                        <Alert variant="warning" className="text-center">Chưa có môn học phụ trách</Alert>
                    ) : (
                        <Table striped bordered hover responsive>
                            <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Mã môn học</th>
                                    <th >Tên môn học</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                {courseSessions.map((course, idx) => (
                                    <tr key={course.id}>
                                        <td>{idx + 1}</td>
                                        <td>{course.code}</td>
                                        <td>{course.courseName}</td>
                                        <td>
                                            <ButtonGroup>
                                                <Button
                                                    variant="info"
                                                    size="sm"
                                                    onClick={() => handleViewStudents(course.id)}
                                                >
                                                    <PeopleFill className="me-1" /> Xem sinh viên
                                                </Button>
                                                <Button
                                                    variant="success"
                                                    size="sm"
                                                    onClick={() => handleManageGrades(course.id)}
                                                >
                                                    <JournalCheck className="me-1" /> Quản lý điểm
                                                </Button>
                                                <Button
                                                    variant="warning"
                                                    size="sm"
                                                    onClick={() => handleManageForum(course.id)}
                                                >
                                                    <ChatDotsFill className="me-1" /> Quản lý diễn đàn
                                                </Button>
                                            </ButtonGroup>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </Table>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default TeacherCourseSessions;
