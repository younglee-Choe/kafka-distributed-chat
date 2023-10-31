import '../../css/Room.css';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Grid, List, ListItem, ListItemText, styled } from '@mui/material';
import axios from 'axios';

const RoomList = () => {
    const navigate = useNavigate();
    const [ chatList, setChatList ] = useState([]);

    const memberIdSession = sessionStorage.getItem("memberId");

    const Demo = styled('div')(({ theme }) => ({
        backgroundColor: theme.palette.background.paper,
        borderColor: 'black',
    }));

    const chatListUrl = "/room/roomList";
    const chatListData = {
        memberId: memberIdSession
    }

    const onClickChatRoomItem = (roomId) => {
        console.log("roomId:", roomId);
        axios.post("/chat/chatRoom", null, {
            params: {
                roomId: roomId
            }
        })
        .then((res) => {
            console.log("res", res);
            navigate("/chat/"+ roomId);
        })
    };

    useEffect(() => {
        axios.post(chatListUrl, chatListData, 
            {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
        .then((res) => {            
            Object.keys(res.data).forEach(roomId => {
                const roomName = res.data[roomId];
   
                setChatList(current => [...current, 
                    {
                        roomId: roomId,
                        roomName: roomName
                    }
                ]);
            });
        });
    }, []);

    return (
        <div className="room__roomList">
            <h2 className="room__roomList-h2">'{memberIdSession}'의 채팅방 목록</h2>
            {chatList.length > 0 ?
                <Grid item xs={12} md={6}>
                    <Demo>
                        <List>
                            {chatList.map((item, idx) => (
                                <ListItem key={idx}>
                                    <ListItemText
                                        id={"room__roomList-item-"+idx}
                                        primary={item.roomName}
                                        secondary={item.roomId}
                                        onClick={() => onClickChatRoomItem(item.roomId)}
                                    />
                                </ListItem>
                            ))}
                        </List>
                    </Demo>
                </Grid>
            :
                <div>채팅방이 없습니다</div>
            }      
        </div>
    )
}

export default RoomList;