import './App.css';
import { useReducer } from 'react';
import MyUserReducer from './reducers/MyUserReducer';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Container } from "react-bootstrap";
import 'bootstrap/dist/css/bootstrap.min.css';
import Header from './components/layout/Header';
import Signup from './components/Signup';
import VerifyOTP from './components/VerifyOTP';

// function App() {
//   return (
//     <div className="App">
//       <header className="App-header">
//         <img src={logo} className="App-logo" alt="logo" />
//         <p>
//           Edit <code>src/App.js</code> and save to reload.
//         </p>
//         <a
//           className="App-link"
//           href="https://reactjs.org"
//           target="_blank"
//           rel="noopener noreferrer"
//         >
//           Learn React
//         </a>
//       </header>
//     </div>
//   );
// }

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);

  return(
    <BrowserRouter>
      <Header />
    
        <Routes>
          {/* <Route path="/" element={<Home />} /> */}
          <Route path="/signup" element={<Signup />} />
          <Route path="/verify-otp" element={<VerifyOTP />} />
        </Routes>

      {/* <Footer /> */}
    </BrowserRouter>
  )
}

export default App;
