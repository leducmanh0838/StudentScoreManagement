export const USER_MAX_AGE = 24 * 60 * 60;
export const UserRoles = {
  ROLE_STUDENT: "student",
  ROLE_TEACHER: "teacher",
  ROLE_ADMIN: "admin",
};
export const WEB_CLIENT_ID = '680589489153-d932o1cnulr5juc3ff04ar2l6eck5ep4.apps.googleusercontent.com'

export const getDefaultAvatar = (firstName, lastName) => {
  const name=`${firstName} ${lastName}`;
  const encodedName = encodeURIComponent(name || "User");
  return `https://ui-avatars.com/api/?name=${encodedName}&background=random`;
};