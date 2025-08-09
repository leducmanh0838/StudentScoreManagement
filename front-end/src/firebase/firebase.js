import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";

// process.env.REACT_APP_GOOGLE_CLIENT_ID

const firebaseConfig = {
  // apiKey: "AIzaSyC1runtSJmblXPYFtoVX1NGZcYLPOM8-bI",
  // authDomain: "final-exam-web-system.firebaseapp.com",
  // projectId: "final-exam-web-system",
  // storageBucket: "final-exam-web-system.firebasestorage.app",
  // messagingSenderId: "219822759312",
  // appId: "1:219822759312:web:b97ef4673f436706066715",
  // measurementId: "G-YF3HQL87QY"
  apiKey: process.env.REACT_APP_FIREBASE_API_KEY,
  authDomain: process.env.REACT_APP_FIREBASE_AUTH_DOMAIN,
  projectId: process.env.REACT_APP_FIREBASE_PROJECT_ID,
  storageBucket: process.env.REACT_APP_FIREBASE_STORAGE_BUCKET,
  messagingSenderId: process.env.REACT_APP_FIREBASE_MESSAGING_SENDER_ID,
  appId: process.env.REACT_APP_FIREBASE_APP_ID,
  measurementId: process.env.REACT_APP_FIREBASE_MEASUREMENT_ID
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
// const analytics = getAnalytics(app);
const db = getFirestore(app);

export { db };