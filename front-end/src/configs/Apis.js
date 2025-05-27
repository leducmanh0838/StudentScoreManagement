import axios from "axios"
import cookie from 'react-cookies'

const BASE_URL = 'http://localhost:8080/FinalExam/api/';

export const endpoints = {
    'login': '/login',
    'signup':'/preStudent/register',
    'current-user':'/secure/user/getCurrentUser',
    'verify':'/preStudent/verify',
    'register-course-session':'/secure/studentAuth/enrollment/register',
    'get-all-course-session-names':'/courses/getAllNames',
    'course-sessions':'courseSessions',
    'get-enrollments-by-student':'/secure/studentAuth/enrollment/getEnrollments',
    'get-grades-by-enrollment':(enrollmentId)=>`/secure/studentAuth/enrollment/${enrollmentId}/getGrades`,
    'course-sessions-by-teacher':'/secure/teacherAuth/courseSessions',
    'get-students-by-course-session':'/secure/teacherAuth/getStudentsByCourseSession',
}

export const authApis = () => {
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Authorization': `Bearer ${cookie.load('token')}`
        }
    })
}

export default axios.create({
    baseURL: BASE_URL
})