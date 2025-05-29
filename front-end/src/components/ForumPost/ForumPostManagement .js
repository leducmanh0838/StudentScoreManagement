import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { Card, Container, Button, Spinner, Image } from 'react-bootstrap';
import { endpoints } from '../../configs/Apis';
import PaginatedData from '../../configs/PaginatedData';
import moment from 'moment';
import 'moment/locale/vi';
import CommentSection from './CommentSection';
import AddForumPostDialog from './AddForumPostDialog';
import DOMPurify from 'dompurify';

const ForumPostManagement = () => {
  console.info('ForumPostManagement: ', Math.random());

  const { courseSessionId } = useParams();

  const [showAddDialog, setShowAddDialog] = useState(false);

  const {
    data: forumPosts,
    setData: setForumPosts,
    loading,
    loadMore,
    refresh,
    hasMore
  } = PaginatedData(
    endpoints['get-forum-posts-by-course-session'](courseSessionId),
    {},
    true
  );

  const defaultAvatar = "https://ui-avatars.com/api/?name=User&background=random";

  const handlePostSuccess = (newPost) => {
    setForumPosts(prev => [newPost, ...prev]);
  };

  return (
    <Container className="mt-4">
      <Card>
        <Card.Header className="bg-info text-white d-flex justify-content-between align-items-center">
          <h5 className="mb-0">Diễn đàn lớp #{courseSessionId}</h5>
          <Button variant="light" size="sm" onClick={() => setShowAddDialog(true)}>
            + Thêm bài viết
          </Button>
        </Card.Header>

        <Card.Body style={{ backgroundColor: '#f8f9fa' }}>
          {forumPosts.length === 0 && !loading && (
            <p className="text-muted text-center">Chưa có bài viết nào.</p>
          )}

          {forumPosts.map(post => (
            <Card key={post.id} className="mb-3 shadow-sm">
              <Card.Body>
                <div className="d-flex mb-2">
                  <Image
                    src={post.user.avatar || defaultAvatar}
                    roundedCircle
                    width={45}
                    height={45}
                    className="me-2"
                  />
                  <div>
                    <strong>{post.user.firstName} {post.user.lastName}</strong><br />
                    <small className="text-muted">{moment(post.createdDate).fromNow()}</small>
                  </div>
                </div>

                <h6 className="mt-2">{post.title}</h6>
                {/* <p>{post.content}</p> */}
                <div dangerouslySetInnerHTML={{
                  // __html: post.content 
                  __html: DOMPurify.sanitize(post.content, {
                    ALLOWED_TAGS: ['b', 'i', 'em', 'strong', 'a', 'p', 'ul', 'ol', 'li', 'br', 'h1', 'h2', 'h3', 'h4', 'img', 'span'],
                    ALLOWED_ATTR: ['href', 'src', 'alt', 'target', 'style']
                  })

                }} />

                <div className="text-end">
                  <CommentSection forumPostId={post.id} />
                </div>
              </Card.Body>
            </Card>
          ))}

          {loading && (
            <div className="text-center my-3">
              <Spinner animation="border" variant="primary" />
            </div>
          )}

          {hasMore && !loading && (
            <div className="text-center mt-3">
              <Button onClick={loadMore} variant="outline-info">
                Xem thêm
              </Button>
            </div>
          )}
        </Card.Body>
      </Card>

      <AddForumPostDialog
        show={showAddDialog}
        handleClose={() => setShowAddDialog(false)}
        courseSessionId={courseSessionId}
        onPostSuccess={handlePostSuccess}
      />
    </Container>
  );
};

export default ForumPostManagement;
