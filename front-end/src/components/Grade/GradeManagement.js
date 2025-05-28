import React, { useEffect, useState } from 'react';
import { Table, Spinner, Alert, Container, Card, Button, Form } from 'react-bootstrap';
import { Link, useParams } from 'react-router-dom';
import { authApis, endpoints } from '../../configs/Apis';
import Papa from 'papaparse';

const GradeManagement = () => {
  const { courseSessionId } = useParams();
  const [criteria, setCriteria] = useState([]);
  const [students, setStudents] = useState([]);
  const [originalScores, setOriginalScores] = useState({}); // Điểm gốc
  const [scores, setScores] = useState({}); // Điểm hiện tại (có thể chỉnh sửa)
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [submitMsg, setSubmitMsg] = useState('');
  const [jsonResult, setJsonResult] = useState({ jsonAdd: null, jsonUpdate: null });
  const [gradeStatus, setGradeStatus] = useState('Draft');

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const criteriaRes = await authApis().get(endpoints['get-criterias-by-course-session'](courseSessionId));
        if (criteriaRes.data.length === 0) {
          return;
        }

        const [studentsRes, gradesRes, gradesStatusRes] = await Promise.all([
          authApis().get(`${endpoints['get-students-by-course-session']}?courseSessionId=${courseSessionId}`),
          // authApis().get(`/secure/teacherAuth/courseSession/${courseSessionId}/getGrades`)
          authApis().get(endpoints['get-grades-by-course-session'](courseSessionId)),
          authApis().get(endpoints['get-grade-status-by-course-session'](courseSessionId))
        ]);

        setGradeStatus(gradesStatusRes.data || 'Draft')
        setCriteria(criteriaRes.data || []);
        setStudents(studentsRes.data || []);

        if (gradesStatusRes.data === 'Locked')
          setSubmitMsg('điểm đã khóa!')

        const gradeData = gradesRes.data || [];

        // Tạo ma trận điểm gốc và ma trận hiện tại
        const initial = {};
        (studentsRes.data || []).forEach(s => {
          initial[s.enrollmentId] = {};
          (criteriaRes.data || []).forEach(c => {
            const found = gradeData.find(
              g => g.enrollmentId === s.enrollmentId && g.criteriaId === c.id
            );
            initial[s.enrollmentId][c.id] = found
              ? { score: found.score, gradeId: found.gradeId }
              : { score: '', gradeId: null };
          });
        });

        // console.info('initial: ', initial);
        // console.info('gradesRes.data: ', gradesRes.data);

        setOriginalScores(initial);
        // Copy nguyên giá trị cho scores
        const clone = JSON.parse(JSON.stringify(initial));
        setScores(clone);
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
        [criteriaId]: {
          ...prev[enrollmentId][criteriaId],
          score: numericValue
        }
      }
    }));
  };

  const handleExportCSV = () => {
    const csvRows = [];

    // Header
    const header = ['#', 'Họ tên', 'Mã sinh viên', 'Email', ...criteria.map(c => `${c.criteriaName} (${c.weight}%)`)];
    csvRows.push(header);

    // Dữ liệu từng sinh viên
    students.forEach((student, index) => {
      const row = [
        index + 1,
        `${student.lastName} ${student.firstName}`,
        student.userCode,
        student.email,
        ...criteria.map(c => {
          const value = scores[student.enrollmentId]?.[c.id]?.score;
          return value !== undefined ? value : '';
        })
      ];
      csvRows.push(row);
    });

    const csvString = '\uFEFF' + Papa.unparse(csvRows);

    const blob = new Blob([csvString], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', `diem_courseSession_${courseSessionId}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };


  // const handleCSVUpload = (e) => {
  //   const file = e.target.files[0];
  //   if (!file) return;

  //   Papa.parse(file, {
  //     header: true,
  //     skipEmptyLines: true,
  //     complete: (results) => {
  //       const parsedData = results.data; // Array các dòng
  //       const newScores = { ...scores };

  //       parsedData.forEach(row => {
  //         const enrollmentId = parseInt(row.enrollmentId);
  //         const criteriaId = parseInt(row.criteriaId);
  //         const score = parseFloat(row.score);

  //         // Nếu tồn tại trong current scores thì gán
  //         if (newScores[enrollmentId] && newScores[enrollmentId][criteriaId]) {
  //           newScores[enrollmentId][criteriaId].score = score;
  //         }
  //       });

  //       setScores(newScores);
  //     },
  //     error: (error) => {
  //       console.error("Lỗi khi đọc file CSV:", error);
  //       alert("Không thể đọc file CSV. Vui lòng kiểm tra định dạng.");
  //     }
  //   });
  // };

  const handleCSVUpload = (e) => {
  const file = e.target.files[0];
  if (!file) return;

  Papa.parse(file, {
    header: true,
    skipEmptyLines: true,
    complete: (results) => {
      const parsedData = results.data;
      const newScores = { ...scores };

      parsedData.forEach(row => {
        const userCode = row['Mã sinh viên']?.trim();
        const student = students.find(s => s.userCode === userCode);

        if (!student) return;

        const enrollmentId = student.enrollmentId;

        // Duyệt từng tiêu chí (criteria)
        criteria.forEach(c => {
          const columnName = `${c.criteriaName} (${c.weight}%)`;
          const scoreValue = row[columnName];

          if (scoreValue !== undefined && scoreValue !== "") {
            const score = parseFloat(scoreValue);

            if (!isNaN(score)) {
              if (!newScores[enrollmentId]) newScores[enrollmentId] = {};
              if (!newScores[enrollmentId][c.id]) newScores[enrollmentId][c.id] = {};

              newScores[enrollmentId][c.id].score = score;
            }
          }
        });
      });

      setScores(newScores);
    },
    error: (error) => {
      console.error("Lỗi khi đọc file CSV:", error);
      alert("Không thể đọc file CSV. Vui lòng kiểm tra định dạng.");
    }
  });
};


  const updateOriginalScores = (responseData) => {
    const originalScoresTemp = JSON.parse(JSON.stringify(originalScores));

    // Duyệt qua mỗi điểm mới được thêm
    responseData.forEach((item) => {
      const { enrollmentId, criteriaId, score, id: gradeId } = item;

      if (!originalScoresTemp[enrollmentId]) {
        originalScoresTemp[enrollmentId] = {};
      }

      originalScoresTemp[enrollmentId][criteriaId] = {
        score,
        gradeId,
      };
    });

    console.info(JSON.stringify(originalScoresTemp,null,2))

    setOriginalScores(originalScoresTemp);

    // Copy luôn sang scores để hiển thị cập nhật
    const scoresCopy = JSON.parse(JSON.stringify(originalScoresTemp));
    setScores(scoresCopy);
  };




  const handleSaveDraft = async () => {
    console.info("lưu nháp")
    const { jsonAdd: jsonRequestAdd, jsonUpdate: jsonRequestUpdate } = getJsonData();

    console.info('jsonRequestAdd: ', JSON.stringify(jsonRequestAdd, null, 2))
    console.info('jsonRequestUpdate: ', JSON.stringify(jsonRequestUpdate, null, 2))

    try {

      if (jsonRequestAdd && jsonRequestAdd.enrollments && jsonRequestAdd.enrollments.length !== 0) {
        let responseAdd = await authApis().post(endpoints['add-grades-by-teacher'], jsonRequestAdd);

        if (responseAdd.status === 200) {
          await updateOriginalScores(responseAdd.data);
          console.info("Thêm điểm thành công!");
        }
      }
      else {
        console.log("JSON Thêm điểm: KHÔNG có điểm mới để thêm.");
      }

      if (jsonRequestUpdate && jsonRequestUpdate.scores && jsonRequestUpdate.scores.length !== 0) {
        let responseAdd = await authApis().post(endpoints['update-grades-by-teacher'], jsonRequestUpdate);

        if (responseAdd.status === 200)
          setOriginalScores(JSON.parse(JSON.stringify(scores)));
          console.info('sửa điểm thành công')
      }
      else {
        console.log("JSON Cập nhật điểm: KHÔNG có điểm nào thay đổi.");
      }

      alert("Lưu nháp thành công!!!");

    } catch (error) {
      if (error.response && error.response.data && error.response.data.message) {
        alert(error.response.data.message);  // hoặc bạn có thể setState để hiển thị trong UI
      } else {
        alert("Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.");
      }
    }
  }

  const handleLockScores = async () => {
    console.info("khóa điểm")
    const { jsonAdd: jsonRequestAdd, jsonUpdate: jsonRequestUpdate } = getJsonData();

    console.info('jsonRequestAdd: ', JSON.stringify(jsonRequestAdd, null, 2))
    console.info('jsonRequestUpdate: ', JSON.stringify(jsonRequestUpdate, null, 2))

    try {

      if (jsonRequestAdd && jsonRequestAdd.enrollments && jsonRequestAdd.enrollments.length !== 0) {
        let responseAdd = await authApis().post(endpoints['add-grades-by-teacher'], jsonRequestAdd);

        if (responseAdd.status === 200) {
          console.info("Thêm điểm thành công!");
        }
      }
      else {
        console.log("JSON Thêm điểm: KHÔNG có điểm mới để thêm.");
      }

      if (jsonRequestUpdate && jsonRequestUpdate.scores && jsonRequestUpdate.scores.length !== 0) {
        let responseAdd = await authApis().post(endpoints['update-grades-by-teacher'], jsonRequestUpdate);

        if (responseAdd.status === 200)
          console.info('sửa điểm thành công')
      }
      else {
        console.log("JSON Cập nhật điểm: KHÔNG có điểm nào thay đổi.");
      }

      let responseLock = await authApis().post(endpoints['lock-grades-by-teacher'](courseSessionId));

      alert("Khóa điểm thành công!!!");
      setGradeStatus('Locked')

    } catch (error) {
      if (error.response && error.response.data && error.response.data.message) {
        alert(error.response.data.message);  // hoặc bạn có thể setState để hiển thị trong UI
      } else {
        alert("Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.");
      }
    }
  }

  const getJsonData = () => {
    const jsonAdd = {
      courseSessionId: parseInt(courseSessionId),
      enrollments: []
    };

    const jsonUpdate = {
      courseSessionId: parseInt(courseSessionId),
      scores: []
    };

    // Duyệt từng enrollmentId trong scores
    for (const enrollmentId in scores) {
      const addScores = [];
      const criteriaScores = scores[enrollmentId];

      // Duyệt từng criteriaId trong từng enrollment
      for (const criteriaId in criteriaScores) {
        const { score, gradeId } = criteriaScores[criteriaId];

        // Lấy điểm gốc để so sánh
        const original = (originalScores[enrollmentId] && originalScores[enrollmentId][criteriaId]) || {};
        console.info('original: ', original);

        // Trường hợp thêm điểm: chưa có gradeId (null hoặc undefined), và có điểm
        if (!gradeId && score !== '') {
          // Nếu điểm gốc giống điểm hiện tại thì không cần thêm
          if (original.score === score) continue;

          addScores.push({
            criteriaId: parseInt(criteriaId),
            score: parseFloat(score)
          });
        }
        // Trường hợp cập nhật điểm: có gradeId và điểm thay đổi
        else if (
          gradeId &&
          score !== '' &&
          parseFloat(score) !== parseFloat(original.score)
        ) {
          jsonUpdate.scores.push({
            gradeId: gradeId,
            score: parseFloat(score)
          });
        }
      }

      // Nếu có điểm cần thêm thì đưa vào enrollments
      if (addScores.length > 0) {
        jsonAdd.enrollments.push({
          enrollmentId: parseInt(enrollmentId),
          scores: addScores
        });
      }
    }

    return { jsonAdd, jsonUpdate };
  };

  const handleShowJson = () => {
    const result = getJsonData();
    setJsonResult(result); // Hiển thị JSON cho người dùng
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
          ) : criteria.length === 0 ? (
            <Alert variant="warning" className="d-flex justify-content-between align-items-center">
              <div>Chưa thêm tiêu chí nào!</div>
              <Link
                to={`/teacher/course-sessions/${courseSessionId}/criterias`}
                className="btn btn-sm btn-outline-primary"
              >
                Quản lý tiêu chí
              </Link>
            </Alert>
          ) : (
            <>
              {submitMsg && <Alert variant="danger">{submitMsg}</Alert>}
              <Table bordered responsive hover>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Họ tên</th>
                    <th>Mã sinh viên</th>
                    <th>Email</th>
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
                      <td>{student.email}</td>
                      {criteria.map(c => (
                        <td key={c.id}>
                          <Form.Control
                            type="number"
                            min="0"
                            max="10"
                            step="0.1"
                            value={scores[student.enrollmentId]?.[c.id]?.score ?? ''}
                            onChange={(e) => handleScoreChange(student.enrollmentId, c.id, e.target.value)}
                            disabled={gradeStatus === 'Locked'}
                          />
                        </td>
                      ))}
                    </tr>
                  ))}
                </tbody>
              </Table>

              {/* Nút lưu nháp và khóa điểm */}
              <div className="d-flex justify-content-end mt-4 gap-2">
                {/* <Button variant="success" onClick={handleExportCSV}>
                  <i className="bi bi-file-earmark-spreadsheet"></i> Xuất CSV
                </Button> */}
                <Button variant="outline-secondary" onClick={handleSaveDraft} hidden={gradeStatus === 'Locked'}>
                  <i className="bi bi-save"></i> Lưu nháp
                </Button>
                <Button variant="primary" onClick={handleLockScores}>
                  <i className="bi bi-lock-fill"></i> Khóa điểm
                </Button>
                <Button variant="success" className="me-2" onClick={handleExportCSV}>
                  Xuất CSV
                </Button>
              </div>
              <Form.Group controlId="formFile" className="mt-4" hidden={gradeStatus === 'Locked'}>
                <Form.Label>Tải lên file CSV điểm</Form.Label>
                <Form.Control type="file" accept=".csv" onChange={handleCSVUpload} />
              </Form.Group>

              {/* <div className="text-end mt-3">
                <Button variant="info" onClick={handleShowJson}>Hiển thị JSON</Button>
              </div>

              {jsonResult.jsonAdd && (
                <>
                  <h5 className="mt-4 text-success">JSON Thêm điểm</h5>
                  <pre>{JSON.stringify(jsonResult.jsonAdd, null, 2)}</pre>
                </>
              )}
              {jsonResult.jsonUpdate && (
                <>
                  <h5 className="mt-4 text-warning">JSON Cập nhật điểm</h5>
                  <pre>{JSON.stringify(jsonResult.jsonUpdate, null, 2)}</pre>
                </>
              )}

              {originalScores && (
                <>
                  <h5 className="mt-4 text-secondary">originalScores (Điểm gốc)</h5>
                  <pre>{JSON.stringify(originalScores, null, 2)}</pre>
                </>
              )}

              {scores && (
                <>
                  <h5 className="mt-4 text-primary">scores (Điểm hiện tại)</h5>
                  <pre>{JSON.stringify(scores, null, 2)}</pre>
                </>
              )} */}
            </>
          )}
        </Card.Body>
      </Card>
    </Container>
  );
};

export default GradeManagement;
