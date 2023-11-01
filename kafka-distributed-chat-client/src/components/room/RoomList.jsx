import '../../css/Room.css';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Grid, List, ListItem, ListItemText, styled } from '@mui/material';
import axios from 'axios';
import enterRoom from '../../api/enterRoom.js';

const RoomList = () => {
    const navigate = useNavigate();
    const [ chatList, setChatList ] = useState([]);
    const [ selected, setSelected ] = useState("");

    const memberId = sessionStorage.getItem("memberId");
    const memberName = sessionStorage.getItem("memberName");

    const croomListUrl = "/room/roomList";
    const roomListData = {
        memberId: memberId
    };

    const Demo = styled('div')(({ theme }) => ({
        backgroundColor: theme.palette.background.paper,
        borderColor: 'black',
    }));

    const onClickChatRoomItem = (roomId) => {
        setSelected(roomId);
        enterRoom(roomId);
    };

    useEffect(() => {
        axios.post(croomListUrl, roomListData, 
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
            <header className="roomlist__mainHeader">
                <h3 className="roomlist__content-container">[<div className="roomlist__content-name">{memberName}</div>]의 채팅방 목록</h3>
            </header>
            {chatList.length > 0 ?
                <Grid item xs={12} md={6} className="room__roomList-grid">
                    <Demo>
                        <List>
                            {chatList.map((item, idx) => (
                                <ListItem key={idx}>
                                    <ListItemText
                                        className={`${selected === item.roomId ? "room__roomList-item__selected" : ""}`}
                                        id={"room__roomList-item-" + idx}
                                        primaryTypographyProps={{fontSize: "1rem"}}
                                        primary={item.roomName}
                                        secondaryTypographyProps={{fontSize: "0.8rem"}}
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