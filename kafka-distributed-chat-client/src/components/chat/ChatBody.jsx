import "../../css/Chat.css";
import React from "react";
import { useNavigate } from "react-router-dom";

const ChatBody = ({ messages }) => {
    const navigate = useNavigate()

    const handleLeaveChat = () => {
        localStorage.removeItem("memberName");
        navigate("/");
        window.location.reload();
    }

    return (
        <>
            <header className="chat__mainHeader">
                <p>채팅방에 있는 멤버들 이름</p>
                <button className="leaveChat__btn" onClick={handleLeaveChat}>
                    채팅방 나가기
                </button>
            </header>

            <div className="message__container">
                {messages.map((message, idx) => 
                    message.memberName === sessionStorage.getItem('memberName') ? 
                    (
                        <div className="message__chats" key={idx}>
                            <p className="sender__name-me">ME({message.memberName})</p>
                            <div className="message__sender">
                                <p>{message.message}</p>
                            </div>
                        </div>
                    ) : (
                        <div className="message__chats" key={idx}>
                            <p className="sender__name-other">{message.memberName}</p>
                            <div className="message__recipient">
                                <p>{message.message}</p>
                            </div>
                        </div>
                    )
                )}
            </div>
        </>
    )
}

export default ChatBody