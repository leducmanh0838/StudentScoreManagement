import { useState, useRef } from "react";
import { MdEmail } from "react-icons/md";
import VerifyOTPStyles from "../../styles/VerifyOTPStyles";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import Apis, { endpoints } from "../../configs/Apis";

const VerifyOTP = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const email = location.state?.email;
    const [otp, setOtp] = useState(Array(6).fill(""));
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);
    const inputsRef = useRef([]);

    const handleChange = (e, index) => {
        const value = e.target.value;
        if (!/^[0-9]?$/.test(value)) return;

        const newOtp = [...otp];
        newOtp[index] = value;
        setOtp(newOtp);

        if (value && index < 5) {
            inputsRef.current[index + 1]?.focus();
        }
    };

    const handleKeyDown = (e, index) => {
        if (e.key === "Backspace" && !otp[index] && index > 0) {
            inputsRef.current[index - 1]?.focus();
        }
    };

    const handleVerify = async (e) => {
        e.preventDefault();
        const code = otp.join("");
        if (code.length !== 6) {
            setError("Vui lòng nhập đủ 6 số.");
            setSuccess(false);
            return;
        }

        console.log({
            email,
            code,
        })

        try {
            // let response = await axios.post("http://localhost:8080/FinalExam/api/preStudent/verify", {
            //     email,
            //     otp: code,
            // });

            let response = await Apis.post(
                endpoints['verify'],
                {
                    email,
                    otp: code,
                }
            );

            setSuccess(true);
            alert("Xác thực thành công!");
            if (response.status === 200)
                navigate("/login");
        } catch (error) {
            if (error.response?.data?.message) {
                setError(error.response.data.message);
            } else {
                setError("Xác thực thất bại. Vui lòng thử lại.");
            }
        }

        // console.log("OTP:", code);
        // setError("");
        // setSuccess(true);
    };

    return (
        <div style={VerifyOTPStyles.wrapper}>
            <div style={VerifyOTPStyles.container}>
                <h2 style={VerifyOTPStyles.title}>
                    <MdEmail style={{ marginRight: 8 }} />
                    Xác thực OTP
                </h2>
                <p style={VerifyOTPStyles.subtitle}>
                    Vui lòng nhập mã OTP được gửi đến email: <strong>{email}</strong>
                </p>
                <form onSubmit={handleVerify} style={VerifyOTPStyles.form}>
                    <div style={VerifyOTPStyles.otpGroup}>
                        {otp.map((digit, idx) => (
                            <input
                                key={idx}
                                type="text"
                                maxLength={1}
                                value={digit}
                                onChange={(e) => handleChange(e, idx)}
                                onKeyDown={(e) => handleKeyDown(e, idx)}
                                ref={(el) => (inputsRef.current[idx] = el)}
                                style={VerifyOTPStyles.otpInput}
                            />
                        ))}
                    </div>
                    <button type="submit" style={VerifyOTPStyles.button}>Xác thực</button>
                </form>
                {error && <p style={{ color: "red", marginTop: 10 }}>{error}</p>}
                {success && <p style={{ color: "green", marginTop: 10 }}>Xác thực thành công!</p>}
            </div>
        </div>
    );
};


export default VerifyOTP;
