import React, { memo, useState } from 'react';
import { Button, Spinner, ListGroup, Image, Collapse } from 'react-bootstrap';
import moment from 'moment';
import 'moment/locale/vi';
import { authApis, endpoints } from '../../configs/Apis';
import PaginatedData from '../../configs/PaginatedData';
import cookie from 'react-cookies';

const CommentSection = ({ forumPostId }) => {
  console.info('CommentSection: ', Math.random());

  const [newComment, setNewComment] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  const [showComments, setShowComments] = useState(true);
  const getDefaultAvatar = (name) => {
    const encodedName = encodeURIComponent(name || "User");
    return `https://ui-avatars.com/api/?name=${encodedName}&background=random`;
  };

  // Dùng custom hook phân trang
  const {
    data: comments,
    setData: setComments,
    loading,
    loadMore,
    refresh,
    hasMore
  } = PaginatedData(endpoints['get-comments-by-forum-post'](forumPostId), {}, true);

  const handleSubmitComment = async () => {
    if (!newComment.trim()) return;

    setSubmitting(true);
    setError(null);
    try {
      let res = await authApis().post(endpoints['add-comment-by-forum-post'](forumPostId), {
        content: newComment.trim()
      });

      const currentUser = cookie.load('user');

      const newPost = {
        ...res.data,
        user: {
          userId: currentUser.userId,
          firstName: currentUser.firstName,
          lastName: currentUser.lastName,
          avatar: currentUser.avatar || null,
        },
        createdDate: Date.now(), // thời gian hiện tại (timestamp)
      };

      setComments(prev => [newPost, ...prev]);
      setNewComment('');
    } catch (err) {
      setError('Gửi bình luận thất bại. Vui lòng thử lại.');
      console.error(err);
    } finally {
      setSubmitting(false);
    }
  };

  const toggleComments = () => {
    setShowComments(!showComments);
    // if (!showComments) {
    //   refresh(); // load lại comments nếu lần đầu bật
    // }
  };

  return (
    <>
      <Button
        variant="outline-secondary"
        size="sm"
        onClick={toggleComments}
        className="mb-2"
      >
        {showComments ? "Ẩn bình luận ▲" : "Xem bình luận ▼"}
      </Button>

      <Collapse in={showComments}>
        <div>
          {loading && comments.length === 0 ? (
            <div className="text-center"><Spinner animation="border" size="sm" /></div>
          ) : comments.length === 0 ? (
            <div className="text-muted">Chưa có bình luận nào.</div>
          ) : (
            <>
              <ListGroup variant="flush">
                {comments.map(comment => (
                  <ListGroup.Item key={comment.id}>
                    <div className="d-flex">
                      <Image
                        src={comment.user.avatar || getDefaultAvatar(`${comment.user.firstName} ${comment.user.lastName}`)}
                        roundedCircle
                        width={40}
                        height={40}
                        className="me-3"
                      />
                      <div className='p-3' style={{
                        backgroundColor: "#f5f5f5",
                        borderRadius: "12px",
                        border: "1px solid #ddd"
                      }}>
                        <div className="d-flex align-items-center mb-1">
                          <strong>{comment.user.firstName} {comment.user.lastName}</strong>
                          <small className="text-muted ms-2">{moment(comment.createdDate).fromNow()}</small>
                        </div>
                        <div style={{ whiteSpace: 'pre-wrap', textAlign: 'left' }}>{comment.content}</div>
                      </div>
                    </div>

                  </ListGroup.Item>
                ))}
              </ListGroup>

              {hasMore && (
                <div className="text-center mt-2">
                  <Button size="sm" variant="outline-primary" onClick={loadMore} disabled={loading}>
                    {loading ? "Đang tải..." : "Xem thêm bình luận"}
                  </Button>
                </div>
              )}
            </>
          )}
        </div>
      </Collapse>
      <div className="mb-3">
        <textarea
          className="form-control"
          placeholder="Viết bình luận..."
          rows={3}
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          disabled={submitting}
        />

        <div className="d-flex justify-content-end mt-2">
          <Button
            size="sm"
            variant="primary"
            onClick={handleSubmitComment}
            disabled={submitting || !newComment.trim()}
          >
            {submitting ? 'Đang gửi...' : 'Gửi bình luận'}
          </Button>
        </div>

        {error && <div className="text-danger mt-1">{error}</div>}
      </div>
    </>
  );
};

// export default memo(CommentSection);
export default CommentSection;
