import React, { useEffect, useState } from 'react';
import { Table, Spinner, Alert, Container, Card, Button, Form } from 'react-bootstrap';
import { useParams } from 'react-router-dom';
import { authApis } from '../../configs/Apis';

const GradeManagement = () => {
  const { courseSessionId } = useParams();
  const [criteria, setCriteria] = useState([]);
  const [students, setStudents] = useState([]);
  const [scores, setScores] = useState({}); // Dạng: {enrollmentId: {criteriaId: score}}
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [submitMsg, setSubmitMsg] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const [criteriaRes, studentsRes] = await Promise.all([
          authApis().get(`/secure/courseSession/${courseSessionId}/getCriterias`),
          authApis().get(`/secure/teacherAuth/getStudentsByCourseSession?courseSessionId=${courseSessionId}`)
        ]);

        setCriteria(criteriaRes.data || []);
        setStudents(studentsRes.data || []);

        // Khởi tạo điểm trống
        const initialScores = {};
        (studentsRes.data || []).forEach(s => {
          initialScores[s.enrollmentId] = {};
          (criteriaRes.data || []).forEach(c => {
            initialScores[s.enrollmentId][c.id] = '';
          });
        });
        setScores(initialScores);
      } catch (err) {
        console.error(err);
        setError("Không thể tải dữ liệu.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [courseSessionId]);

  const handleScoreChange = (enrollmentId, criteriaId, value) => {
    const numericValue = value === '' ? '' : parseFloat(value);
    setScores(prev => ({
      ...prev,
      [enrollmentId]: {
        ...prev[enrollmentId],
        [criteriaId]: numericValue
      }
    }));
  };

  const handleSubmit = async () => {
    const payload = {
      courseSessionId: parseInt(courseSessionId),
      enrollments: Object.entries(scores)
        .map(([enrollmentId, criteriaScores]) => ({
          enrollmentId: parseInt(enrollmentId),
          scores: Object.entries(criteriaScores)
            .filter(([_, score]) => score !== '') // bỏ điểm trống
            .map(([criteriaId, score]) => ({
              criteriaId: parseInt(criteriaId),
              score: parseFloat(score)
            }))
        }))
        .filter(enrollment => enrollment.scores.length > 0) // bỏ các enrollment không có score
    };
    console.info('payload:', JSON.stringify(payload, null, 2));

    try {
      await authApis().post('/secure/teacherAuth/grade/addGrades', payload); // <-- endpoint mẫu
      setSubmitMsg('Lưu điểm thành công!');
    } catch (error) {

      if (error.response && error.response.data && error.response.data.message) {
        setSubmitMsg(error.response.data.message);
      } else {
        setSubmitMsg("Lỗi khi lưu điểm!");
      }

      console.error("Login error:", error.response || error.message);

    }
  };

  return (
    <Container className="mt-4">
      <Card>
        <Card.Header className="bg-primary text-white">
          <h5>Quản lý điểm</h5>
        </Card.Header>
        <Card.Body>
          {loading ? (
            <div className="text-center">
              <Spinner animation="border" />
              <p>Đang tải dữ liệu...</p>
            </div>
          ) : error ? (
            <Alert variant="danger">{error}</Alert>
          ) : (
            <>
              {submitMsg && <Alert variant="info">{submitMsg}</Alert>}
              <Table bordered responsive hover>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Họ tên</th>
                    <th>Mã sinh viên</th>
                    {criteria.map(c => (
                      <th key={c.id}>
                        {c.criteriaName} <br />
                        <small>({c.weight}%)</small>
                      </th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {students.map((student, idx) => (
                    <tr key={student.userId}>
                      <td>{idx + 1}</td>
                      <td>{student.lastName} {student.firstName}</td>
                      <td>{student.userCode}</td>
                      {criteria.map(c => (
                        <td key={c.id}>
                          <Form.Control
                            type="number"
                            min="0"
                            max="10"
                            step="0.1"
                            value={scores[student.enrollmentId]?.[c.id] ?? ''}
                            onChange={(e) => handleScoreChange(student.enrollmentId, c.id, e.target.value)}
                          />
                        </td>
                      ))}
                    </tr>
                  ))}
                </tbody>
              </Table>
              <div className="text-end mt-3">
                <Button variant="success" onClick={handleSubmit}>Lưu điểm</Button>
              </div>
            </>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
};

export default GradeManagement;
