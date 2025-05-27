import { Spinner } from "react-bootstrap";

const MySpinner = () => {
    return (
        <div className="d-flex justify-content-center">
            <Spinner animation="border" role="status" variant="danger">
                <span className="visually-hidden">Đang tải...</span>
            </Spinner>
        </div>
    );
}

export default MySpinner;