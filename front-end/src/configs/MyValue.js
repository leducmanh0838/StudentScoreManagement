export const USER_MAX_AGE = 24 * 60 * 60;
export const UserRoles = {
  ROLE_STUDENT: "student",
  ROLE_TEACHER: "teacher",
  ROLE_ADMIN: "admin",
};

export const WEB_CLIENT_ID = process.env.REACT_APP_GOOGLE_CLIENT_ID;

export const getDefaultAvatar = (firstName, lastName) => {
  const name=`${firstName} ${lastName}`;
  const encodedName = encodeURIComponent(name || "User");
  return `https://ui-avatars.com/api/?name=${encodedName}&background=random`;
};