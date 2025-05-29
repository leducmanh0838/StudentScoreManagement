import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";

const firebaseConfig = {
  apiKey: "AIzaSyC1runtSJmblXPYFtoVX1NGZcYLPOM8-bI",
  authDomain: "final-exam-web-system.firebaseapp.com",
  projectId: "final-exam-web-system",
  storageBucket: "final-exam-web-system.firebasestorage.app",
  messagingSenderId: "219822759312",
  appId: "1:219822759312:web:b97ef4673f436706066715",
  measurementId: "G-YF3HQL87QY"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
// const analytics = getAnalytics(app);
const db = getFirestore(app);

export { db }; // export theo kiểu ESM