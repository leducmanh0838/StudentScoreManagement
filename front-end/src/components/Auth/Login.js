import { useContext, useState } from "react";
import { Form, Button, Container, Alert } from "react-bootstrap";
import SignupStyles from "../../styles/SignupStyles";
import { useNavigate, useSearchParams } from "react-router-dom";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { MyDispatchContext } from "../../configs/Contexts";
import cookie from 'react-cookies'
import { USER_MAX_AGE, WEB_CLIENT_ID } from "../../configs/MyValue";
import { GoogleLogin, GoogleOAuthProvider } from "@react-oauth/google";

const Login = () => {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [errors, setErrors] = useState({});
  const [submitError, setSubmitError] = useState("");
  const [submitSuccess, setSubmitSuccess] = useState(false);
  const [q] = useSearchParams();
  const dispatch = useContext(MyDispatchContext);

  const validate = () => {
    const newErrors = {};
    if (!formData.email) newErrors.email = "Email không được để trống";
    else if (!/\S+@\S+\.\S+/.test(formData.email)) newErrors.email = "Email không hợp lệ";

    if (!formData.password) newErrors.password = "Mật khẩu không được để trống";
    else if (formData.password.length < 6) newErrors.password = "Mật khẩu phải từ 6 ký tự trở lên";

    return newErrors;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: null }));
    setSubmitError("");
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
      let response = await Apis.post(
        endpoints['login'],
        {
          email: formData.email,
          password: formData.password,
        }
      );

      cookie.save('token', response.data.token, {
        path: ',',
        maxAge: USER_MAX_AGE
      });

      let currentUser = await authApis().get(endpoints['current-user']);

      console.log("Login success:", response.data);

      console.info(currentUser.data);
      dispatch({
        "type": "login",
        "payload": currentUser.data
      });

      cookie.save('user', currentUser.data, {
        path: ',',
        maxAge: USER_MAX_AGE
      });


      let next = q.get('next');
      if (next)
        navigate(next);
      else
        navigate('/');

    } catch (error) {
      setSubmitSuccess(false);

      if (error.response && error.response.data && error.response.data.message) {
        setSubmitError(error.response.data.message);
      } else {
        setSubmitError("Đã xảy ra lỗi không xác định. Vui lòng thử lại sau.");
      }

      console.error("Login error:", error.response || error.message);
    }
  };

  const handleSuccess = async (credentialResponse) => {
    const idToken = credentialResponse.credential;
    try {
      const res = await Apis.post(endpoints['auth-google'], {
        idToken,
      });
      console.log("token id:", res.data);
      const token = res.data.token;

      cookie.save('token', token, {
        path: ',',
        maxAge: USER_MAX_AGE
      });

      let currentUser = await authApis().get(endpoints['current-user']);

      console.log("Login success:", currentUser.data);

      console.info(currentUser.data);
      dispatch({
        "type": "login",
        "payload": currentUser.data
      });

      cookie.save('user', currentUser.data, {
        path: ',',
        maxAge: USER_MAX_AGE
      });

      navigate('/');

      // console.log("Token  backend:", res.data.token);
    } catch (error) {
      if (error.response && error.response.data && error.response.data.message) {
        alert(error.response.data.message);
      }
      if (error.response) {
        console.error("Lỗi backend:", error.response.data);
      } else if (error.request) {
        console.error("Không nhận được phản hồi từ server:", error.request);
      } else {
        console.error("Lỗi khi gửi yêu cầu:", error.message);
      }
    }
  };

  return (
    <div style={SignupStyles.wrapper}>
      <Container style={SignupStyles.container}>
        <h2 className="mb-4 text-center">Đăng nhập tài khoản</h2>

        {submitSuccess && (
          <Alert variant="success" onClose={() => setSubmitSuccess(false)} dismissible>
            Đăng nhập thành công!
          </Alert>
        )}

        {submitError && (
          <Alert variant="danger" onClose={() => setSubmitError("")} dismissible>
            {submitError}
          </Alert>
        )}

        <Form onSubmit={handleSubmit} noValidate>
          <Form.Group className="mb-3" controlId="email">
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

          <Form.Group className="mb-4" controlId="password">
            <Form.Label>Mật khẩu</Form.Label>
            <Form.Control
              type="password"
              placeholder="Nhập mật khẩu"
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

          <Button type="submit" variant="primary" className="w-100 rounded-pill py-2 mb-3">
            Đăng nhập
          </Button>

          <GoogleOAuthProvider clientId={WEB_CLIENT_ID}>
            <GoogleLogin
              onSuccess={handleSuccess}
              onError={() => {
                console.log("Login thất bại");
              }}
            />
          </GoogleOAuthProvider>
        </Form>
      </Container>
    </div>
  );
};

export default Login;
