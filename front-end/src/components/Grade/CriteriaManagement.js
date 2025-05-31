import React, { useState, useEffect } from "react";
import { Button, Form, Table, Alert, Container, Card } from "react-bootstrap";
import { authApis, endpoints } from "../../configs/Apis";
import { useParams } from "react-router-dom";

const CriteriaManagement = () => {
    const { courseSessionId } = useParams();

    const [criterias, setCriterias] = useState([
        { criteriaName: "midterm", weight: 0 },
        { criteriaName: "final", weight: 0 },
    ]);
    const [criteriaName, setCriteriaName] = useState("");
    const [weight, setWeight] = useState("");
    const [locked, setLocked] = useState(false);
    const [error, setError] = useState("");

    useEffect(() => {
        const fetchCriterias = async () => {
            try {
                const res = await authApis().get(endpoints['get-criterias-by-course-session'](courseSessionId));
                if (res.data && res.data.length > 0) {
                    setCriterias(res.data.map(c => ({
                        criteriaName: c.criteriaName,
                        weight: c.weight
                    })));
                    setLocked(true);
                }
            } catch (err) {
                console.error("Lỗi khi lấy tiêu chí:", err);
            }
        };

        fetchCriterias();
    }, [courseSessionId]);

    const handleAdd = () => {
        setError("");

        if (!criteriaName.trim() || weight === "") {
            setError("Vui lòng nhập đầy đủ tên và trọng số.");
            return;
        }

        if (criterias.length >= 5) {
            setError("Chỉ được thêm tối đa 5 tiêu chí.");
            return;
        }

        const w = parseFloat(weight);
        if (isNaN(w) || w <= 0) {
            setError("Trọng số phải là số lớn hơn 0.");
            return;
        }

        if (criterias.some(c => c.criteriaName.toLowerCase() === criteriaName.trim().toLowerCase())) {
            setError("Tiêu chí này đã tồn tại.");
            return;
        }

        const totalWeight = criterias.reduce((sum, c) => sum + c.weight, 0) + w;
        if (totalWeight > 100) {
            setError("Tổng trọng số không được vượt quá 100%.");
            return;
        }

        setCriterias([...criterias, { criteriaName: criteriaName.trim(), weight: w }]);
        setCriteriaName("");
        setWeight("");
    };

    const handleDelete = (index) => {
        if (locked) return;
        // Không xóa midterm và final
        const critName = criterias[index].criteriaName.toLowerCase();
        if (critName === "midterm" || critName === "final") return;

        const newCriterias = [...criterias];
        newCriterias.splice(index, 1);
        setCriterias(newCriterias);
        setError("");
    };

    const handleWeightChange = (index, newWeight) => {
        if (locked) return;
        const w = parseFloat(newWeight);
        if (isNaN(w) || w < 0) {
            setError("Trọng số phải là số hợp lệ và không âm.");
            return;
        }
        const newCriterias = [...criterias];
        newCriterias[index].weight = w;
        setCriterias(newCriterias);
        setError("");
    };

    const handleConfirm = async () => {
        setError("");
        if (criterias.length === 0) {
            setError("Chưa có tiêu chí nào để xác nhận!");
            return;
        }

        const totalWeight = criterias.reduce((sum, c) => sum + c.weight, 0);
        if (totalWeight !== 100) {
            setError("Tổng trọng số phải đúng bằng 100%");
            return;
        }

        const isConfirmed = window.confirm(
            "Bạn có chắc chắn muốn xác nhận các tiêu chí này? Sau khi xác nhận, sẽ không thể chỉnh sửa nữa."
        );
        if (!isConfirmed) {
            return;
        }

        try {
            await authApis().post(endpoints['add-criterias-by-teacher'](courseSessionId), criterias);
            setLocked(true);
        } catch (error) {
            if (error.response && error.response.data && error.response.data.message) {
                setError(error.response.data.message);
            } else {
                setError("Hệ thống có lỗi");
            }
        }
    };

    return (
        <Container className="mt-4">
            <Card>
                <Card.Header className="bg-success text-white">
                    <h5>Thêm tiêu chí chấm điểm</h5>
                </Card.Header>
                <Card.Body>
                    {error && <Alert variant="danger">{error}</Alert>}

                    {!locked && (
                        <Form className="d-flex gap-3 mb-3" onSubmit={e => { e.preventDefault(); handleAdd(); }}>
                            <Form.Control
                                type="text"
                                placeholder="Tên tiêu chí"
                                value={criteriaName}
                                onChange={(e) => setCriteriaName(e.target.value)}
                                maxLength={50}
                            />
                            <Form.Control
                                type="number"
                                placeholder="Trọng số (%)"
                                value={weight}
                                min="1"
                                max="100"
                                step="0.1"
                                onChange={(e) => setWeight(e.target.value)}
                            />
                            <Button variant="primary" type="submit">
                                Thêm
                            </Button>
                        </Form>
                    )}

                    <Table bordered hover>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Tên tiêu chí</th>
                                <th>Trọng số (%)</th>
                                {!locked && <th>Hành động</th>}
                            </tr>
                        </thead>
                        <tbody>
                            {criterias.map((c, idx) => (
                                <tr key={idx}>
                                    <td>{idx + 1}</td>
                                    <td>{c.criteriaName}</td>
                                    <td>
                                        {!locked ? (
                                            <Form.Control
                                                type="number"
                                                min="0"
                                                max="100"
                                                step="0.1"
                                                value={c.weight}
                                                onChange={(e) => handleWeightChange(idx, e.target.value)}
                                            />
                                        ) : (
                                            c.weight
                                        )}
                                    </td>
                                    {!locked && (c.criteriaName.toLowerCase() !== "midterm" && c.criteriaName.toLowerCase() !== "final") && (
                                        <td>
                                            <Button
                                                variant="danger"
                                                size="sm"
                                                onClick={() => handleDelete(idx)}
                                            >
                                                Xóa
                                            </Button>
                                        </td>
                                    )}
                                </tr>
                            ))}
                        </tbody>
                    </Table>

                    {!locked && (
                        <div className="text-end">
                            <Button variant="success" onClick={handleConfirm}>
                                Xác nhận tiêu chí
                            </Button>
                        </div>
                    )}

                    {locked && (
                        <Alert variant="info" className="text-center mt-3">
                            Đã xác nhận, không thể chỉnh sửa tiêu chí nữa.
                        </Alert>
                    )}
                </Card.Body>
            </Card>
        </Container>
    );
};

export default CriteriaManagement;
