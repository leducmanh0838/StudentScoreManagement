import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import { Card, Button, Spinner } from "react-bootstrap";
import { authApis, endpoints } from "../../configs/Apis";
import { getDefaultAvatar } from "../../configs/MyValue";

const UserProfile = () => {
  const { userId } = useParams();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); // để hiển thị "Loading..."
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const res = await authApis().get(endpoints['get-user-info'](userId));
        setUser(res.data);
      } catch (err) {
        setError("Không thể tải thông tin người dùng.");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchUserInfo();
  }, [userId]);

  if (loading) return <div className="text-center mt-5"><Spinner animation="border" /> Đang tải...</div>;
  if (error) return <div className="text-danger text-center mt-5">{error}</div>;
  if (!user) return null;

  const fullName = `${user.firstName} ${user.lastName}`;
  const avatarUrl = user.avatar || getDefaultAvatar(user.firstName, user.lastName);

  return (
    <div className="container mt-4">
      <Card className="shadow border-0">
        {/* Cover image */}
        <div
          style={{
            height: "200px",
            backgroundImage: "url('https://picsum.photos/1200/400')",
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        ></div>

        {/* Avatar and Name Section */}
        <Card.Body className="text-center position-relative" style={{ marginTop: "-75px" }}>
          <img
            src={avatarUrl}
            alt="Avatar"
            className="rounded-circle border border-white shadow"
            style={{ width: "150px", height: "150px", objectFit: "cover" }}
          />
          <h3 className="mt-3">{fullName}</h3>
          <p className="text-muted mb-1">{user.email}</p>
          <p className="text-muted">
            👤 Mã: {user.userCode}
          </p>
        </Card.Body>
      </Card>
    </div>
  );
};

export default UserProfile;
