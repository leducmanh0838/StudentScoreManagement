import React, { useEffect, useState } from 'react';
import { Table, Spinner, Alert, Container, Card, Form } from 'react-bootstrap';
import { useParams } from 'react-router-dom';
import { authApis } from '../../configs/Apis';

const GradeManagement = () => {
  const { courseSessionId } = useParams();
  const [criteria, setCriteria] = useState([]);
  const [students, setStudents] = useState([]);
  const [scores, setScores] = useState({}); // Dạng: { enrollmentId: { criteriaId: { score, gradeId } } }
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const [criteriaRes, studentsRes, gradesRes] = await Promise.all([
          authApis().get(`/secure/courseSession/${courseSessionId}/getCriterias`),
          authApis().get(`/secure/teacherAuth/getStudentsByCourseSession?courseSessionId=${courseSessionId}`),
          authApis().get(`/secure/teacherAuth/courseSession/${courseSessionId}/getGrades`)
        ]);

        const criterias = criteriaRes.data || [];
        const studentsData = studentsRes.data || [];
        const grades = gradesRes.data || [];

        console.info('criterias: ',criterias)

        setCriteria(criterias);
        setStudents(studentsData);

        const initialScores = {};
        studentsData.forEach((student) => {
          initialScores[student.enrollmentId] = {};
          criterias.forEach((criteriaItem) => {
            initialScores[student.enrollmentId][criteriaItem.id] = {
              score: '',
              gradeId: undefined,
            };
          });
        });

        // map dữ liệu điểm đã có vào
        grades.forEach((grade) => {
          const { enrollmentId, criteriaId, gradeId, score } = grade;
          if (initialScores[enrollmentId] && initialScores[enrollmentId][criteriaId]) {
            initialScores[enrollmentId][criteriaId] = {
              score,
              gradeId,
            };
          }
        });

        setScores(initialScores);
      } catch (err) {
        console.error(err);
        setError('Không thể tải dữ liệu.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [courseSessionId]);

  const handleScoreChange = (enrollmentId, criteriaId, value) => {
    const numericValue = value === '' ? '' : parseFloat(value);
    setScores((prev) => ({
      ...prev,
      [enrollmentId]: {
        ...prev[enrollmentId],
        [criteriaId]: {
          ...prev[enrollmentId][criteriaId],
          score: numericValue,
        },
      },
    }));
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
            <Table bordered responsive hover>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Họ tên</th>
                  <th>Mã sinh viên</th>
                  {criteria.map((c) => (
                    <th key={c.id}>
                      {c.criteriaName}
                      <br />
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
                    {criteria.map((c) => (
                      <td key={c.id}>
                        <Form.Control
                          type="number"
                          min="0"
                          max="10"
                          step="0.1"
                          value={scores[student.enrollmentId]?.[c.id]?.score ?? ''}
                          onChange={(e) =>
                            handleScoreChange(student.enrollmentId, c.id, e.target.value)
                          }
                        />
                      </td>
                    ))}
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

export default GradeManagement;
