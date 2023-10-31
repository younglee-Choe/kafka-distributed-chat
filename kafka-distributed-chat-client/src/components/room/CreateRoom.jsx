import '../../css/Chat.css';
import 'react-toastify/dist/ReactToastify.css';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, TextField } from '@mui/material';
import { ToastContainer, toast } from 'react-toastify';
import axios from 'axios';

const CreateRoom = () => {
  const navigate = useNavigate();
  const [ roomName, setRoomName ] = useState('');
  const [ isClick, setClick ] = useState(false);

  const createRoomUrl = "/room/createRoom";
  const memberId = sessionStorage.getItem("memberId");

  const memberIdSession = sessionStorage.getItem("memberId");

  const createRoomHandler = (roomName) => {
    if(roomName !== "" && memberId !== null) {
      setClick(true);
      axios.post(createRoomUrl, null, {
        params: {
          roomName: roomName,
          memberId: memberId
        }
      })
      .then((res) => {
        console.log("create room:", res);
        navigate("/room/roomList");
      })
      .catch((err) => console.log("An error occurred while creating the room! ", err));
    } else if(memberId === null) {
      toast.warn(<div><h4>채팅방 생성 실패</h4>로그인이 필요합니다</div>);
    } else {
      toast.warn(<div><h4>채팅방 생성 실패</h4>채팅방 이름을 설정해주세요</div>);
    }
  };

  return (
    <div className="room__createRoom">
      <h2 className="room__createRoom-h2">Create Room</h2>
      <div className="room__createRoom-input-container">
        <TextField 
          id="room__input"
          label="Room name"
          variant="filled"
          name="room name"
          type="text"
          onChange={(e) => setRoomName(e.target.value)}
          value={roomName}
        />
        <div className="room__createRoom-btn-container">        
          {isClick ? 
            <Button variant="contained" disabled>
              생성됨
            </Button>
          : 
            <Button 
            variant="contained"
            onClick={() => createRoomHandler(roomName)}
          >
            채팅방 만들기
          </Button> 
          }
        </div>
        <ToastContainer
          position="top-right"
          autoClose={3500}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="light"
        />  
      </div>
    </div>
  )
}

export default CreateRoom;