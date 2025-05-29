import cookie from 'react-cookies'

export default (current, action) => {
    switch (action.type) {
        case "login":
            return action.payload;
        case "logout":
            cookie.remove('token');
            cookie.remove('user');
            return null;
    }
    return current;
}