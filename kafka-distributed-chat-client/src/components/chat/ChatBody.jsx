import "../../css/Chat.css";
import React, { useRef, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

const ChatBody = ({ messages }) => {
    const navigate = useNavigate();
    const [ memberList, setMemberList ] = useState([]);

    const scrollRef = useRef();

    const roomId = useParams().roomId;
    const memberName = sessionStorage.getItem("memberName");

    const memberListUrl = "/room/memberList";
    const memberListData = {
        roomId: roomId, 
    }

    const handleLeaveChat = () => {
        localStorage.removeItem("memberName");
        navigate("/");
        window.location.reload();

        // 추가) DB에서 해당 채팅방 삭제하도록 서버에 요청하는 코드 작성
    }

    useEffect(() => {
        axios.post(memberListUrl, memberListData, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then((res) => {
            Object.keys(res.data).forEach(memberId => {
                const memberName = res.data[memberId];
   
                setMemberList(current => [...current, 
                    {
                        memberId: memberId,
                        memberName: memberName
                    }
                ]);
            });
        })
        .catch((err) => console.log("An error occurred while retrieving the list of members currently in the room! ", err));
    }, []);

    useEffect(() => {
        scrollRef.current.scrollTop = scrollRef.current.scrollHeight;
    }, [messages]);

    return (
        <>
            <header className="chat__mainHeader">
                <div className="chat__memberList-container">
                    <div style={{ color:"black", fontWeight:"900", paddingRight:"0.5rem" }}>대화 상대</div>[
                    {memberList.map((member, idx) => 
                        (idx === memberList.length - 1 ? 
                            <div 
                                className="chat__memberList-item"
                                key={idx} 
                            >{member.memberName} </div>
                        :
                            <div 
                                className="chat__memberList-item"
                                key={idx} 
                            >{member.memberName}, </div>
                        )
                    )}
                    ]
                </div>
                <button className="leaveChat__btn" onClick={handleLeaveChat}>
                    채팅방 나가기
                </button>
            </header>

            <div className="message__container" ref={scrollRef}>
                {messages.map((message, idx) => 
                    message.memberName === sessionStorage.getItem('memberName') ? 
                    (
                        <div className="message__chats" key={idx}>
                            <p className="sender__name-me">{message.memberName}(나)</p>
                            <div className="message__content-me">
                                <div className="message__date-me">{message.date}</div>
                                <div className="message__sender">
                                    <p>{message.message}</p>
                                </div>
                            </div>
                        </div>
                    ) : (
                        <div className="message__chats" key={idx}>
                            <p className="sender__name-other">{message.memberName}</p>
                            <div className="message__content-other">
                                <div className="message__recipient">
                                    <p>{message.message}</p>
                                </div>
                                <div className="message__date-other">{message.date}</div>
                            </div>
                        </div>
                    )
                )}
            </div>
        </>
    )
}

export default ChatBody