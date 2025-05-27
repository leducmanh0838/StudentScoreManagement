const VerifyOTPStyles = {
  wrapper: {
    minHeight: "100vh",
    background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
    display: "flex",
    alignItems: "flex-start",
    justifyContent: "center",
    padding: 20,
  },
  container: {
    backgroundColor: "#fff",
    borderRadius: 16,
    padding: 40,
    boxShadow: "0 12px 30px rgba(0, 0, 0, 0.2)",
    width: "50%", // Chiếm 50% chiều ngang
    maxWidth: 700,
    textAlign: "center",
    marginTop: 60,
  },
  title: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    fontSize: 28,         // Tăng cỡ chữ
    fontWeight: "bold",
    marginBottom: 15,
  },
  subtitle: {
    fontSize: 18,         // Tăng cỡ chữ
    marginBottom: 25,
  },
  form: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  otpGroup: {
    display: "flex",
    gap: 15,              // Khoảng cách lớn hơn giữa các ô
    justifyContent: "center",
    marginBottom: 30,
  },
  otpInput: {
    width: 60,            // Ô to hơn
    height: 70,
    fontSize: 28,
    textAlign: "center",
    borderRadius: 8,
    border: "2px solid #ccc",
    outline: "none",
  },
  button: {
    backgroundColor: "#667eea",
    color: "#fff",
    padding: "12px 30px",
    fontSize: 18,
    fontWeight: "bold",
    border: "none",
    borderRadius: 8,
    cursor: "pointer",
    transition: "background 0.3s ease",
  },
};
export default VerifyOTPStyles;