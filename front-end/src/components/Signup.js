import { useState } from "react";
import { Form, Button, Container, Row, Col, Alert } from "react-bootstrap";
import SignupStyles from "../styles/SignupStyles";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Signup = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        confirmPassword: "",
        avatar: null,
    });

    function isStrongPassword(password) {
        if (!password || password.length < 6) return false;

        const hasUpper = /[A-Z]/.test(password);
        const hasLower = /[a-z]/.test(password);
        const hasDigit = /[0-9]/.test(password);
        const hasSpecial = /[!@#$%^&*()\-_=+\[\]{}|;:'",.<>?/`~]/.test(password);

        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    const [errors, setErrors] = useState({});
    const [submitSuccess, setSubmitSuccess] = useState(false);
    const [avatarPreview, setAvatarPreview] = useState(null);

    const validate = () => {
        const newErrors = {};
        if (!formData.firstName.trim()) newErrors.firstName = "First Name không được để trống";
        if (!formData.lastName.trim()) newErrors.lastName = "Last Name không được để trống";
        if (!formData.email) newErrors.email = "Email không được để trống";
        else if (!/\S+@\S+\.\S+/.test(formData.email)) newErrors.email = "Email không hợp lệ";
        if (!formData.password) newErrors.password = "Mật khẩu không được để trống";
        else if (formData.password.length < 6) newErrors.password = "Mật khẩu phải từ 6 ký tự trở lên";
        else if (!isStrongPassword(formData.password)) newErrors.password = "Mật khẩu phải có chữ hoa, chữ thường, số và ký tự đặc biệt";
        if (formData.confirmPassword !== formData.password) newErrors.confirmPassword = "Mật khẩu xác nhận không khớp";
        return newErrors;
    };

    const handleChange = (e) => {
        const { name, value, files } = e.target;

        if (name === "avatar") {
            const file = files[0] || null;
            setFormData((prev) => ({ ...prev, avatar: file }));
            setAvatarPreview(file ? URL.createObjectURL(file) : null);  // tạo preview
            setErrors((prev) => ({ ...prev, avatar: null }));
        } else {
            setFormData((prev) => ({ ...prev, [name]: value }));
            setErrors((prev) => ({ ...prev, [name]: null }));           // xoá lỗi field tương ứng
        }
    };


    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validate();
        if (Object.keys(validationErrors).length > 0) {
            setErrors(validationErrors);
            setSubmitSuccess(false);
            return;
        }

        try {
            // Tạo đối tượng FormData để gửi file và dữ liệu text
            const data = new FormData();
            data.append("firstName", formData.firstName);
            data.append("lastName", formData.lastName);
            data.append("email", formData.email);
            data.append("password", formData.password);
            if (formData.avatar) {
                data.append("avatar", formData.avatar);
            }

            // Gửi POST request
            const response = await axios.post(
                "http://localhost:8080/FinalExam/api/preStudent/register",
                data,
                {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                }
            );

            console.log("Response from server:", response.data);

            navigate("/verify-otp", { state: { email: formData.email } });

            // setSubmitSuccess(true);
            // setFormData({
            //     firstName: "",
            //     lastName: "",
            //     email: "",
            //     password: "",
            //     confirmPassword: "",
            //     avatar: null,
            // });
            // setAvatarPreview(null);
            // setErrors({});
        } catch (error) {
            setSubmitSuccess(false);

            // Nếu là lỗi từ phía server có message, ví dụ email đã tồn tại
            if (error.response && error.response.data && error.response.data.message) {
                alert(error.response.data.message);  // hoặc bạn có thể setState để hiển thị trong UI
            } else {
                alert("Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.");
            }

            console.error("Error response:", error.response || error.message);
        }
    };

    return (
        <div style={SignupStyles.wrapper}>
            <Container style={SignupStyles.container}>
                <h2 className="mb-4 text-center">Đăng ký tài khoản</h2>

                {submitSuccess && (
                    <Alert
                        variant="success"
                        onClose={() => setSubmitSuccess(false)}
                        dismissible
                    >
                        Đăng ký thành công! Vui lòng kiểm tra email để xác nhận.
                    </Alert>
                )}

                <Form onSubmit={handleSubmit} noValidate>
                    <Row className="mb-3">
                        <Col>
                            <Form.Group controlId="firstName">
                                <Form.Label>Tên</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Nhập tên"
                                    name="firstName"
                                    value={formData.firstName}
                                    onChange={handleChange}
                                    isInvalid={!!errors.firstName}
                                    style={SignupStyles.roundedInput}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.firstName}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Col>

                        <Col>
                            <Form.Group controlId="lastName">
                                <Form.Label>Họ</Form.Label>
                                <Form.Control
                                    type="text"
                                    placeholder="Nhập họ"
                                    name="lastName"
                                    value={formData.lastName}
                                    onChange={handleChange}
                                    isInvalid={!!errors.lastName}
                                    style={SignupStyles.roundedInput}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.lastName}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Col>
                    </Row>

                    <Row className="mb-3">
                        <Col>
                            <Form.Group controlId="email">
                                <Form.Label>Email</Form.Label>
                                <Form.Control
                                    type="email"
                                    placeholder="Nhập email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    isInvalid={!!errors.email}
                                    style={SignupStyles.roundedInput}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.email}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Col>
                    </Row>

                    <Row className="mb-3">
                        <Col>
                            <Form.Group controlId="password">
                                <Form.Label>Mật khẩu</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Mật khẩu"
                                    name="password"
                                    value={formData.password}
                                    onChange={handleChange}
                                    isInvalid={!!errors.password}
                                    style={SignupStyles.roundedInput}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.password}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Col>
                    </Row>

                    <Row className="mb-3">
                        <Col>
                            <Form.Group controlId="confirmPassword">
                                <Form.Label>Xác nhận mật khẩu</Form.Label>
                                <Form.Control
                                    type="password"
                                    placeholder="Nhập lại mật khẩu"
                                    name="confirmPassword"
                                    value={formData.confirmPassword}
                                    onChange={handleChange}
                                    isInvalid={!!errors.confirmPassword}
                                    style={SignupStyles.roundedInput}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.confirmPassword}
                                </Form.Control.Feedback>
                            </Form.Group>
                        </Col>
                    </Row>

                    <Row className="mb-4">
                        <Col>
                            <Form.Group controlId="avatar">
                                <Form.Label>Avatar</Form.Label>
                                <Form.Control
                                    type="file"
                                    accept="image/*"
                                    name="avatar"
                                    onChange={handleChange}
                                    style={SignupStyles.roundedInput}
                                />
                            </Form.Group>
                        </Col>
                    </Row>

                    {avatarPreview && (
                        <div className="mb-3" style={{ textAlign: "center" }}>
                            <img
                                src={avatarPreview}
                                alt="Avatar Preview"
                                style={SignupStyles.avatar}
                            />
                        </div>
                    )}

                    <Button type="submit" variant="primary" className="w-100 rounded-pill py-2 mb-3">
                        Đăng ký
                    </Button>

                    {/* Nút đăng ký bằng Google */}
                    <Button
                        type="button"
                        variant="outline-danger"
                        className="w-100 rounded-pill py-2 d-flex align-items-center justify-content-center"
                        // onClick={handleGoogleSignup}
                        style={{ gap: 10 }}
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 48 48" width="24px" height="24px"><path fill="#FFC107" d="M43.611,20.083H42V20H24v8h11.303c-1.649,4.657-6.08,8-11.303,8c-6.627,0-12-5.373-12-12c0-6.627,5.373-12,12-12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C12.955,4,4,12.955,4,24c0,11.045,8.955,20,20,20c11.045,0,20-8.955,20-20C44,22.659,43.862,21.35,43.611,20.083z" /><path fill="#FF3D00" d="M6.306,14.691l6.571,4.819C14.655,15.108,18.961,12,24,12c3.059,0,5.842,1.154,7.961,3.039l5.657-5.657C34.046,6.053,29.268,4,24,4C16.318,4,9.656,8.337,6.306,14.691z" /><path fill="#4CAF50" d="M24,44c5.166,0,9.86-1.977,13.409-5.192l-6.19-5.238C29.211,35.091,26.715,36,24,36c-5.202,0-9.619-3.317-11.283-7.946l-6.522,5.025C9.505,39.556,16.227,44,24,44z" /><path fill="#1976D2" d="M43.611,20.083H42V20H24v8h11.303c-0.792,2.237-2.231,4.166-4.087,5.571c0.001-0.001,0.002-0.001,0.003-0.002l6.19,5.238C36.971,39.205,44,34,44,24C44,22.659,43.862,21.35,43.611,20.083z" /></svg>
                        Đăng ký bằng Google
                    </Button>



                </Form>
            </Container>
        </div>
    );
};

export default Signup;