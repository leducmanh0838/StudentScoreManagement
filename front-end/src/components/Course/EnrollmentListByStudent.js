import React, { useEffect, useState } from 'react';
import cookie from 'react-cookies';
import { authApis, endpoints } from '../../configs/Apis';
import { FaComments, FaEye } from 'react-icons/fa';
import { Modal, Button, Spinner, Alert } from 'react-bootstrap'; // cần cài react-bootstrap
import { Link } from 'react-bootstrap-icons';
import { useNavigate } from 'react-router-dom';

const EnrollmentListByStudent = () => {
  const nav = useNavigate();
  const [enrollments, setEnrollments] = useState([]);
  const [loading, setLoading] = useState(true);

  const [showModal, setShowModal] = useState(false);
  const [grades, setGrades] = useState([]);
  const [gradeLoading, setGradeLoading] = useState(false);
  const [gradeError, setGradeError] = useState(null);

  useEffect(() => {
    const fetchEnrollments = async () => {
      try {
        const response = await authApis().get(endpoints['get-enrollments-by-student']);
        setEnrollments(response.data);
      } catch (error) {
        console.error('Failed to fetch enrollments:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchEnrollments();
  }, []);

  const formatDate = (timestamp) => {
    if (!timestamp) return 'Chưa có ngày';
    const date = new Date(timestamp);
    return date.toLocaleDateString();
  };

  const handleViewForumPost = async (courseSessionId) =>{
    nav(`/course-sessions/${courseSessionId}/forum-posts`);
  }

  // Mở modal và gọi API lấy điểm
  const handleViewDetail = async (enrollmentId) => {
    setShowModal(true);
    setGradeLoading(true);
    setGradeError(null);
    setGrades([]);

    try {
      const response = await authApis().get(endpoints['get-grades-by-enrollment'](enrollmentId));
      const gradesData = response.data;

      // Thay null thành 0
      const gradesWithNoNull = gradesData.map(g => ({
        ...g,
        score: g.score === null ? 0 : g.score
      }));

      // Tính tổng điểm
      const totalWeight = gradesWithNoNull.reduce((sum, g) => sum + g.weight, 0);
      const totalScore = totalWeight > 0
        ? gradesWithNoNull.reduce((sum, g) => sum + g.score * g.weight, 0) / totalWeight
        : 0;

      // Thêm 1 phần tử điểm cuối cùng (total)
      const gradesWithTotal = [...gradesWithNoNull, {
        criteria: 'Tổng điểm',
        score: totalScore,
        weight: totalWeight
      }];

      setGrades(gradesWithTotal);
    } catch (error) {
      // if (error.response && error.response.status >= 400 && error.response.status < 500) {
      //   // Lỗi 4xx có thể là điểm chưa công bố
      //   setGradeError('Điểm chưa được công bố.');
      // } else {
      //   setGradeError('Lỗi không xác định khi lấy điểm.');
      // }
      if (error.response) {
        const status = error.response.status;
        const data = error.response.data;
        if (status >= 400 && status < 500) {
          // Lấy message server trả về hoặc fallback text
          const msg = data.message || "Không thể xem điểm do lỗi từ client.";
          setGradeError(msg);
        } else {
          setGradeError("Lỗi máy chủ, vui lòng thử lại sau.");
        }
      }
    } finally {
      setGradeLoading(false);
    }
  };

  return (
    <div className="container mt-5">
      <div className="card shadow-lg border-0">
        <div className="card-header bg-primary text-white text-center">
          <h3 className="mb-0">📚 Danh sách môn học đã học</h3>
        </div>
        <div className="card-body">
          {loading ? (
            <div className="alert alert-info text-center">Đang tải dữ liệu...</div>
          ) : enrollments.length === 0 ? (
            <div className="alert alert-warning text-center">Không có môn học nào được ghi danh.</div>
          ) : (
            <div className="table-responsive">
              <table className="table table-striped align-middle">
                <thead className="table-light">
                  <tr>
                    <th scope="col">#</th>
                    <th scope="col">Tên môn học</th>
                    <th scope="col">Ngày ghi danh</th>
                    <th scope="col" className="text-center">Hành động</th>
                  </tr>
                </thead>
                <tbody>
                  {enrollments.map((item, index) => (
                    <tr key={item.enrollmentId}>
                      <td>{index + 1}</td>
                      <td>{item.courseName}</td>
                      <td>{formatDate(item.enrollmentDate)}</td>
                      <td className="text-center d-flex justify-content-center gap-2">
                        <button
                          className="btn btn-outline-primary btn-sm d-flex align-items-center gap-1"
                          onClick={() => handleViewDetail(item.enrollmentId)}
                        >
                          <FaEye /> Xem chi tiết
                        </button>
                        <button
                          className="btn btn-outline-success btn-sm d-flex align-items-center gap-1"
                          onClick={() => handleViewForumPost(item.courseSessionId)}
                        >
                          <FaComments /> Xem diễn đàn
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* Modal hiển thị điểm */}
      <Modal show={showModal} onHide={() => setShowModal(false)} size="lg" centered>
        <Modal.Header closeButton>
          <Modal.Title>Chi tiết điểm</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {gradeLoading ? (
            <div className="text-center">
              <Spinner animation="border" variant="primary" />
              <p>Đang tải điểm...</p>
            </div>
          ) : gradeError ? (
            <Alert variant="warning" className="text-center">{gradeError}</Alert>
          ) : grades.length === 0 ? (
            <p className="text-center">Không có dữ liệu điểm.</p>
          ) : (
            <table className="table table-bordered">
              <thead>
                <tr className='table-success'>
                  <th>Tiêu chí</th>
                  <th>Điểm</th>
                  <th>Trọng số (%)</th>
                </tr>
              </thead>
              <tbody>
                {grades.map((grade, index) => {
                  const isTotal = grade.criteria === 'Tổng điểm';
                  return (
                    <tr key={index} className={isTotal ? 'fw-bold text-dark' : ''}>
                      <td>{grade.criteria}</td>
                      <td>{grade.score !== null ? grade.score : 'Chưa có điểm'}</td>
                      <td>{grade.weight}</td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Đóng
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default EnrollmentListByStudent;
