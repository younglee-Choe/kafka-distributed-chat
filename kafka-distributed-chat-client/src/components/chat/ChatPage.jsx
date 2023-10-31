import '../../css/Chat.css';
import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import ChatBody from './ChatBody';
import ChatFooter from './ChatFooter';
import RoomList from '../room/RoomList';

// listen to the message from the server and display it to all users
const ChatPage = () => {
    const [ messages, setMessages ] = useState([])
    const [ typingStatus, setTypingStatus ] = useState([])
    const lastMessageRef = useRef(null)

    useEffect(() => {    
        axios.get("/chat")
        .then((res) => console.log("Create Room; ", res))
        .catch(error => console.log("Create Room error: ", error));

        lastMessageRef.current?.scrollIntoView({ behavior: 'smooth' });

    }, [messages]);

    return (
        <div className="chat">
            <RoomList />
            <div className="chat__main">
                <ChatBody messages={messages} typingStatus={typingStatus} lastMessageRef={lastMessageRef} />
                <ChatFooter />
            </div>
        </div>
    )
}

export default ChatPage