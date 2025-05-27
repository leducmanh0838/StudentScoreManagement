import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import Apis, { authApis, endpoints } from '../../configs/Apis';
import { Alert } from 'react-bootstrap';
import UsePaginatedData from '../../configs/UsePaginatedData';

const CourseSessionList = () => {
  const [courses, setCourses] = useState([]);
  const [selectedCourseId, setSelectedCourseId] = useState('');
  const [message, setMessage] = useState(null);

  const {
    data: sessions,
    loading,
    loadMore,
    refresh,
    hasMore
  } = UsePaginatedData(endpoints['course-sessions'], selectedCourseId ? { courseId: selectedCourseId } : {});

  useEffect(() => {
    Apis.get(endpoints['get-all-course-session-names'])
      .then(res => setCourses(res.data))
      .catch(() => setMessage({ type: 'danger', text: 'Không thể tải danh sách môn học.' }));
  }, []);

  const handleCourseChange = (e) => {
    const id = e.target.value;
    setSelectedCourseId(id);
  };

  const handleRegister = async (id) => {
    try {
      await authApis().post(endpoints['register-course-session'], {
        courseSessionId: id
      });
      setMessage(null);
      alert('Đăng ký thành công');
    } catch (error) {
      setMessage(null);
      if (error.response?.data?.message) {
        alert(error.response.data.message);
      } else {
        alert("Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.");
      }
      console.error("Error response:", error.response || error.message);
    }
  };

  return (
    <div className="container py-5">
      <h2 className="text-center fw-bold mb-4">📚 Đăng ký môn học</h2>

      {message && (
        <Alert
          variant={message.type}
          onClose={() => setMessage(null)}
          dismissible
        >
          {message.text}
        </Alert>
      )}

      <div className="mb-4">
        <select
          className="form-select"
          value={selectedCourseId}
          onChange={handleCourseChange}
        >
          <option value="">-- Chọn môn học --</option>
          {courses.map(course => (
            <option key={course.id} value={course.id}>{course.name}</option>
          ))}
        </select>
      </div>

      <div className="row">
        {sessions.map(session => (
          <div className="col-md-6 col-lg-4 mb-4" key={session.id}>
            <div className="card shadow-sm rounded-4 h-100">
              <div className="card-body d-flex flex-column">
                <div className="mb-2">
                  <span className="badge bg-info text-dark mb-2">{session.courseName}</span>
                  <h5 className="card-title text-primary">{session.code}</h5>
                  <p className="card-text text-muted mb-1">
                    <i className="bi bi-person-fill"></i> {session.teacherName}
                  </p>
                  <p className="card-text">
                    {session.isOpen ? (
                      <span className="badge bg-success">Đang mở</span>
                    ) : (
                      <span className="badge bg-secondary">Đã đóng</span>
                    )}
                  </p>
                </div>
                <div className="mt-auto">
                  <button
                    className="btn btn-outline-primary w-100"
                    onClick={() => handleRegister(session.id)}
                    disabled={!session.isOpen}
                  >
                    Đăng ký ngay
                  </button>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      {hasMore && (
        <div className="text-center mt-4">
          <button
            className="btn btn-primary px-4"
            onClick={loadMore}
            disabled={loading}
          >
            {loading ? 'Đang tải...' : 'Xem thêm'}
          </button>
        </div>
      )}
    </div>
  );
};

export default CourseSessionList;
