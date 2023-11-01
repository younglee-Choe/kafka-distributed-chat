import '../../css/Chat.css';
import 'react-toastify/dist/ReactToastify.css';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, TextField } from '@mui/material';
import { ToastContainer, toast } from 'react-toastify';
import enterRoom from '../../api/enterRoom';

const SearchRoom = () => {
  const navigate = useNavigate();
  const [ roomId, setRoomId ] = useState('');
  const [ isClick, setClick ] = useState(false);

  const memberId = sessionStorage.getItem("memberId");

  const searchRoomHandler = (roomId) => {
    if(roomId !== "" && memberId !== null) {
        setClick(true);
        enterRoom(roomId);
    } else if(memberId === null) {
        toast.warn(<div><h4>채팅방 검색&입장 실패</h4>로그인이 필요합니다</div>);
    } else {
        toast.error(<div><h4>채팅방 검색&입장 실패</h4>채팅방 ID를 다시 입력해 주세요</div>);
    }
  };

  return (
    <div className="room__createRoom">
      <h2 className="room__createRoom-h2">🔎 Room Search & Enter</h2>
      <div className="room__createRoom-input-container">
        <TextField 
            className="room__createRoom__input"
          id="room__input"
          label="채팅방 ID를 입력해 주세요"
          variant="filled"
          name="room id"
          type="text"
          onChange={(e) => setRoomId(e.target.value)}
          value={roomId}
        />
        <div className="room__createRoom-btn-container">        
            {isClick ? 
                <Button variant="contained" disabled>
                채팅방 입장
                </Button>
            : 
                <Button 
                    variant="contained"
                    onClick={() => searchRoomHandler(roomId)}
                >
                    채팅방 입장
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

export default SearchRoom;