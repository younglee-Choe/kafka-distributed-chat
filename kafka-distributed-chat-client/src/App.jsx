import './css/App.css';
import React, { useEffect, useState } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Header from './components/header/Header';
import LogIn from './components/auth/LogIn';
import SignUp from './components/auth/SignUp';
import CreateRoom from './components/room/CreateRoom';
import RoomList from './components/room/RoomList';
import SearchRoom from './components/room/SearchRoom';
import ChatPage from './components/chat/ChatPage';

function App() {
  const memberIdSession = sessionStorage.getItem("memberId");
  const [ isSession, setSession ] = useState(false); 

  useEffect(() => {
    if(memberIdSession !== null) {
      setSession(true);
    }
  }, [memberIdSession, isSession])

  return (
    <div className="App">
      <BrowserRouter>
        <div className="app__main">
          <div className="app__header">
            {isSession ?
              <Header />
              :
              <></>
            }
          </div>
          <div className="app__body">
            <Routes>
              <Route path="/room/createRoom" element={<CreateRoom/>}></Route>
              <Route path="/room/roomList" element={<RoomList/>}></Route>
              <Route path="/room/searchRoom" element={<SearchRoom/>}></Route>
              <Route path="/sub/chat/:roomId" element={<ChatPage/>}></Route>
              <Route path="/signup" element={<SignUp/>}></Route>
              <Route path="/" element={<LogIn/>}></Route>
            </Routes>
          </div>
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App
