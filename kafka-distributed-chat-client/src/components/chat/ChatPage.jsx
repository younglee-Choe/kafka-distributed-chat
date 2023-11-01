import '../../css/Chat.css';
import React, { useRef, useState, useEffect } from "react";
import { useParams } from "react-router-dom"; 
import * as StompJs from "@stomp/stompjs";
import ChatBody from './ChatBody';
import RoomList from '../room/RoomList';

// listen to the message from the server and display it to all users
// 웹소켓에 연결할 때 STOMP 클라이언트 생성
function ChatPage() {
    const [ chatList, setChatList ] = useState([]);
    const [ message, setMessages ] = useState('');
  
    // const roomId = useParams();
    const roomId = {roomId};
    const memberName = sessionStorage.getItem("memberName");
    const date = new Date();
    const client = useRef({});
  
    const connect = () => { // 연결할 때
      client.current = new StompJs.Client({
        brokerURL: "ws://localhost:8080/ws",
        onConnect: () => {
          console.log("Successful connection!", roomId);
          subscribe();
        },
      });
      client.current.activate(); // 클라이언트 활성화
    };
  
    const publish = (message) => {
      if (!client.current.connected) return; // 연결되지 않았으면 메시지를 보내지 않음
  
      client.current.publish({
        destination: '/pub/chat',
        body: JSON.stringify({
          roomId: roomId,
          memberName: memberName,
          message: message,
          date: date
        }), // 형식에 맞게 수정해서 보내야 함
      });
  
      setMessages('');
    };
  
    const subscribe = () => {   // 연결 성공 시 채널 구독하고 메시지 받음
      client.current.subscribe("/sub/chat/" + roomId, (body) => {
        // 메시지의 payload는 body.body에 실려옴
        const json_body = JSON.parse(body.body);
        setChatList((_msg_list) => [
            ..._msg_list, json_body
        ]);
      });
    };
  
    const handleChange = (e) => { // 채팅 입력 시 state 값 설정
      setMessages(e.target.value);
    };
  
    const handleSubmit = (e, message) => { // 보내기 버튼 눌렀을 때 publish
      e.preventDefault();
  
      publish(message);
    };
  
    const disconnect = () => { // 연결이 끊겼을 때 
      client.current.deactivate();
    };
  
    useEffect(() => {
      connect();
      
      return () => disconnect();
    }, []);
    return (
      <div className="chat">
        <RoomList />
        <div className="chat__main">
          <ChatBody messages={chatList} />
          <div className="chat__footer">
            <form className="form" onSubmit={(e) => handleSubmit(e, message)}>
              <input
                type="text"
                placeholder="Write message"
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