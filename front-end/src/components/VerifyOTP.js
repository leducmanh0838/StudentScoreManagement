import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import SignupStyles from "../styles/SignupStyles";
import { Container } from "react-bootstrap";

const VerifyOTP = () => {
    const { state } = useLocation();
    const navigate = useNavigate();
    const email = state?.email || "";

    const [otp, setOtp] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);

    const handleVerify = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/FinalExam/api/preStudent/verify", {
                email,
                otp,
            });
            setSuccess(true);
            alert("Xác thực thành công!");
            navigate("/login");  // hoặc trang chính
        } catch (error) {
            if (error.response?.data?.message) {
                setError(error.response.data.message);
            } else {
                setError("Xác thực thất bại. Vui lòng thử lại.");
            }
        }
    };

    return (
        <div style={SignupStyles.wrapper}>
            <Container style={SignupStyles.container}>
                <div>
                    <h2>Xác thực OTP</h2>
                    <p>Vui lòng nhập mã OTP được gửi đến email: <strong>{email}</strong></p>
                    <form onSubmit={handleVerify}>
                        <input
                            type="text"
                            value={otp}
                            onChange={(e) => setOtp(e.target.value)}
                            placeholder="Nhập mã OTP"
                            required
                        />
                        <button type="submit">Xác thực</button>
                    </form>
                    {error && <p style={{ color: "red" }}>{error}</p>}
                    {success && <p style={{ color: "green" }}>Xác thực thành công!</p>}
                </div>
            </Container>
        </div>

    );
};

export default VerifyOTP;
