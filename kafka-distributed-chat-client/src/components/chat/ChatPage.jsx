import '../../css/Chat.css';
import React, { useRef, useState, useEffect } from "react";
import { useParams } from "react-router-dom"; 
import * as StompJs from "@stomp/stompjs";
import ChatBody from './ChatBody';
import RoomList from '../room/RoomList';
import axios from 'axios';
import moment from 'moment/moment';

// listen to the message from the server and display it to all users
// 웹소켓에 연결할 때 STOMP 클라이언트 생성
function ChatPage() {
    const [ chatList, setChatList ] = useState([]);
    const [ message, setMessages ] = useState("");
    const [ subscription, setSubscription ] = useState("");
    const [ files, setFiles ] = useState(null);

    const roomId = useParams().roomId;
    const memberId = sessionStorage.getItem("memberId");
    const memberName = sessionStorage.getItem("memberName");
    const currentDate = new Date();
    const date = moment(currentDate).format("YYYY-MM-DDTHH:mm:ss.SSSSSS");
    const client = useRef({});
  
    const historyUrl = "/chat/history/" + memberId;

    const connect = () => { // 연결할 때
      client.current = new StompJs.Client({
        brokerURL: "ws://localhost:8080/ws",
        onConnect: () => {
          console.log("Successful connection!", roomId);
          enterSubscribe();
          initSubscribe();
          subscribe();
        },
        onWebSocketError: (error) => {
          console.log("❗️Failed to connect: ", error);
          disconnect();
        }
      });
      client.current.activate(); // 클라이언트 활성화
    };
  
    const publish = (message) => {
      subscription.unsubscribe();
      if (!client.current.connected) return; // 연결되지 않았으면 메시지를 보내지 않음
      console.log("publish: ", message);
      client.current.publish({
        destination: '/pub/chat',
        body: JSON.stringify({
          roomId: roomId,
          memberId: memberId,
          memberName: memberName,
          message: message,
          date: date,
        }),
      });
  
      setMessages('');
    };

    const enterSubscribe = () => {
      setSubscription(client.current.subscribe("/sub/chat/entry/" + roomId, (body) => {
        const json_body = JSON.parse(body.body);
        console.log("enter message: ", json_body);
        setChatList((_msg_list) => [
          ..._msg_list, json_body
        ]);
      }));
    };

    // 채팅방에 재입장 시, 이전 채팅 기록 요청
    const initSubscribe = () => {
      setSubscription(client.current.subscribe("/sub/chat/init/" + roomId + memberId, (body) => {
        const json_body = JSON.parse(body.body);
        json_body.forEach(element => {
          setChatList((_msg_list) => [
            ..._msg_list, element
          ]);
        });
      }));
    };

    const subscribe = () => {   // 연결 성공 시 채널 구독하고 메시지 받음
      client.current.subscribe("/sub/chat/" + roomId, (body) => {
        // 메시지의 payload는 body.body에 실려옴
        const json_body = JSON.parse(body.body);
        console.log("subscribe: ", json_body);
        setChatList((_msg_list) => [
          ..._msg_list, json_body
        ]);
      });
    };
  
    const handleChange = (e) => {
      setMessages(e.target.value);
    };
  
    const handleSubmit = (e, message) => { // 보내기 버튼 눌렀을 때 publish
      e.preventDefault();

      if(message !== "") {  
        publish(message);
      }
    };

    const clickUploadBtn = () => {
      document.getElementById('fileInput').click();
    };

    const handleChangeFile = (event) => {
      setFiles(event.target.files);
    };

    const handleClearFiles = () => {
      setFiles(null);
    };

    const handleFileUpload = () => {
      //파일이 한 개 이상 있을 경우
      if(files) {
        const formData = new FormData();
        // Post 요청에 함께 보낼 formData 작성(입력한 파일 추가)
        for (let i = 0; i < files.length; i++) {
          formData.append("files", files[i]);
        }
        // Fast API로 요청(업로드)
        axios.post("http://localhost:8000/file/upload", formData, 
          {headers: { "Content-Type": "multipart/form-data" }})
          .then(response => {
            if(response.data) {
              setFiles(null);
              alert("✅ " + response.data.message);
            }
          })
          .catch((error) => {
            alert("업로드 실패: " + error);
          })
      } 
      // 입력한 파일이 한 개도 없을 경우 
      else {
        alert("파일을 1개 이상 넣어주세요!")
      }
    }
  
    const disconnect = () => { // 연결이 끊겼을 때 
      client.current.deactivate();
    };

    const initHistory = () => {
      axios.get(historyUrl, {
        params: {
          roomId: roomId,
        }
      })
      .then((res) => {
        console.log(res);
      })
      .catch((err) => console.log("[ERROR] An error occurred while loading previous conversation contents: ", err));
    }
  
    useEffect(() => {
      connect();
      initHistory();

      return () => disconnect();
    }, [memberId]);
    
    return (
      <div className="chat">
        <RoomList />
        <div className="chat__main">
          <ChatBody messages={chatList} />
          <div className="chat__footer">
            <div className="chat__file">
              <input
                type="file"
                id="fileInput"
                onChange={handleChangeFile}
                multiple
                style={{ display: 'none' }}
              />
              <button id="uploadBtnClick" onClick={clickUploadBtn}>파일 선택</button>
              {files && (files.length >= 1 ) ? (
                <div className="file-list">
                  <ul>
                    {Array.from(files).map((file, index) => (
                    <li key={index}>{file.name}</li>
                    ))}
                  </ul>
                  <button onClick={handleClearFiles} className="button">
                    선택된 파일 지우기
                  </button>
                </div>
              ) : (
                <p></p>
              )}
              {/* 파일 업로드 버튼 */}
              <button onClick={handleFileUpload} className="upload-button">
                파일 업로드
              </button>
            </div>
            <form className="form" onSubmit={(e) => handleSubmit(e, message)}>
              <input
                type="text"
                placeholder="메시지 입력"
                className="message"
                value={message}
                onChange={handleChange}
                // onKeyDown={handleTyping}
              />
              <button className="sendBtn">보내기</button>
            </form>
          </div>
        </div>
      </div>
    )
}

export default ChatPage