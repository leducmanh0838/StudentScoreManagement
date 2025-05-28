import React, { useState } from "react";
import { Button, Form, Table, Alert, Container, Card } from "react-bootstrap";
import { authApis, endpoints } from "../../configs/Apis";
import { useParams } from "react-router-dom";

const CriteriaManagement = () => {
    const { courseSessionId } = useParams();
    const [criterias, setCriterias] = useState([]);
    const [criteriaName, setCriteriaName] = useState("");
    const [weight, setWeight] = useState("");
    const [locked, setLocked] = useState(false);
    const [error, setError] = useState("");

    const handleAdd = () => {
        if (!criteriaName || weight === "") {
            setError("Vui lòng nhập đầy đủ tên và trọng số.");
            return;
        }

        const w = parseFloat(weight);
        const totalWeight = criterias.reduce((sum, c) => sum + c.weight, 0) + w;

        if (totalWeight > 100) {
            setError("Tổng trọng số không được vượt quá 100%");
            return;
        }

        setCriterias([...criterias, { criteriaName, weight: w }]);
        setCriteriaName("");
        setWeight("");
        setError("");
    };

    const handleConfirm = async () => {
        console.info(JSON.stringify(criterias, null, 2));
        if (criterias.length === 0) {
            setError("Chưa có tiêu chí nào để xác nhận!");
            return;
        }

        const totalWeight = criterias.reduce((sum, c) => sum + c.weight, 0);
        if (totalWeight !== 100) {
            setError("Tổng trọng số phải đúng bằng 100%");
            return;
        }


        try {
            const res = await authApis().post(endpoints['add-criterias-by-teacher'](courseSessionId), criterias);

            setLocked(true);
            setError("");
        } catch (error) {
            if (error.response && error.response.data && error.response.data.message) {
                setError(error.response.data.message)
            }

            setError("Hệ thống có lỗi")
            // if (onConfirm) onConfirm(criterias); // callback để lưu lên server nếu cần
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
                        <>
                            <Form className="d-flex gap-3 mb-3">
                                <Form.Control
                                    type="text"
                                    placeholder="Tên tiêu chí"
                                    value={criteriaName}
                                    onChange={(e) => setCriteriaName(e.target.value)}
                                />
                                <Form.Control
                                    type="number"
                                    placeholder="Trọng số (%)"
                                    value={weight}
                                    min="1"
                                    max="100"
                                    step="1"
                                    onChange={(e) => setWeight(e.target.value)}
                                />
                                <Button variant="primary" onClick={handleAdd}>
                                    Thêm
                                </Button>
                            </Form>
                        </>
                    )}

                    <Table bordered hover>
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Tên tiêu chí</th>
                                <th>Trọng số (%)</th>
                            </tr>
                        </thead>
                        <tbody>
                            {criterias.map((c, idx) => (
                                <tr key={idx}>
                                    <td>{idx + 1}</td>
                                    <td>{c.criteriaName}</td>
                                    <td>{c.weight}</td>
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
