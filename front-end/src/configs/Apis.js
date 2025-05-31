import axios from "axios"
import cookie from 'react-cookies'

const BASE_URL = 'http://localhost:8080/FinalExam/api/';

export const endpoints = {
    //Auth
    'login': '/login',
    'signup': '/preStudent/register',
    'current-user': '/secure/user/getCurrentUser',
    'verify': '/preStudent/verify',
    'find-users':'/secure/user/findUsers',
    'auth-google':'/auth/google',
    'get-user-info':(userId) => `/secure/user/${userId}/getUserInfo`,

    //Enrollment
    'get-enrollments-by-student': '/secure/studentAuth/enrollment/getEnrollments',
    'get-grades-by-enrollment': (enrollmentId) => `/secure/studentAuth/enrollment/${enrollmentId}/getGrades`,

    // Course session
    'register-course-session': '/secure/studentAuth/enrollment/register',
    'get-all-course-session-names': '/courses/getAllNames',
    'course-sessions': 'courseSessions',
    'course-sessions-by-teacher': '/secure/teacherAuth/courseSessions',
    'get-students-by-course-session': '/secure/teacherAuth/getStudentsByCourseSession',

    // Grade
    'get-grades-by-course-session': (courseSessionId) => `/secure/teacherAuth/courseSession/${courseSessionId}/getGrades`,
    'add-grades-by-teacher': '/secure/teacherAuth/grade/addGrades',
    'update-grades-by-teacher': '/secure/teacherAuth/grade/updateGrades',
    'lock-grades-by-teacher': (courseSessionId) => `/secure/teacherAuth/courseSession/${courseSessionId}/lock`,
    'get-grade-status-by-course-session': (courseSessionId) => `/secure/teacherAuth/courseSession/${courseSessionId}/getGradeStatus`,

    // Criteria
    'get-criterias-by-course-session': (courseSessionId) => `/secure/courseSession/${courseSessionId}/getCriterias`,
    'add-criterias-by-teacher': (courseSessionId) => `/secure/teacherAuth/courseSession/${courseSessionId}/addCriterias`,

    //Forum post
    'get-forum-posts-by-course-session': (courseSessionId) => `/secure/courseSession/${courseSessionId}/getForumPosts`,
    'add-forum-post-by-course-session': (courseSessionId) => `/secure/courseSession/${courseSessionId}/addForumPost`,

    // Comment
    'get-comments-by-forum-post': (forumPostId) => `/secure/forumPost/${forumPostId}/getComments`,
    'add-comment-by-forum-post': (forumPostId) => `/secure/forumPost/${forumPostId}/addComment`,
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