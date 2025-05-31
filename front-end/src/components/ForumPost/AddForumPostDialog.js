import React, { memo, useRef, useState } from 'react';
import { Modal, Button, Form, Alert, Spinner } from 'react-bootstrap';
import axios from 'axios';
import { authApis, endpoints } from '../../configs/Apis';
import cookie from 'react-cookies';
import JoditEditor from 'jodit-react';

const AddForumPostDialog = ({ courseSessionId, show, handleClose, onPostSuccess }) => {
    console.info('AddForumPostDialog: ', Math.random());

    const editor = useRef(null);


    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async () => {
        if (!title.trim() || !content.trim()) {
            setError('Vui lòng nhập đầy đủ tiêu đề và nội dung.');
            return;
        }

        setLoading(true);
        setError(null);
        try {
            let res = await authApis().post(
                endpoints['add-forum-post-by-course-session'](courseSessionId),
                {
                    title,
                    content,
                }
            );

            const currentUser = cookie.load('user');

            const newPost = {
                ...res.data,
                user: {
                    userId: currentUser.userId,
                    firstName: currentUser.firstName,
                    lastName: currentUser.lastName,
                    avatar: currentUser.avatar || null,
                },
                createdDate: Date.now(),
            };

            setTitle('');
            setContent('');

            onPostSuccess(newPost);
            handleClose();
        } catch (error) {
            if (error.response && error.response.data && error.response.data.message)
                setError(error.response.data.message);
            else
                setError('Đã xảy ra lỗi khi thêm bài viết.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Thêm bài viết mới</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                {error && <Alert variant="danger">{error}</Alert>}
                <Form>
                    <Form.Group className="mb-3">
                        <Form.Label>Tiêu đề</Form.Label>
                        <Form.Control
                            type="text"
                            value={title}
                            onChange={e => setTitle(e.target.value)}
                            placeholder="Nhập tiêu đề bài viết"
                        />
                    </Form.Group>
                    {/* <Form.Group className="mb-3">
                        <Form.Label>Nội dung</Form.Label>
                        <Form.Control
                            as="textarea"
                            rows={4}
                            value={content}
                            onChange={e => setContent(e.target.value)}
                            placeholder="Nhập nội dung bài viết"
                        />
                    </Form.Group> */}
                    <Form.Group className="mb-3">
                        <Form.Label>Nội dung</Form.Label>
                        <JoditEditor
                            ref={editor}
                            value={content}
                            tabIndex={1}
                            onBlur={newContent => setContent(newContent)}
                        />
                    </Form.Group>

                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose} disabled={loading}>
                    Hủy
                </Button>
                <Button variant="primary" onClick={handleSubmit} disabled={loading}>
                    {loading ? <Spinner animation="border" size="sm" /> : 'Đăng bài'}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

// export default AddForumPostDialog;
export default memo(AddForumPostDialog);
