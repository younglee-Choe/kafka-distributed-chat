import "../../css/Header.css";
import React from "react";
import { Link } from 'react-router-dom';
import { category } from "../../Settings";

function NavBar() {
    const navHeader = [
        { name: "Home", path: "/", number: category.Home },
        { name: "채팅방 생성", path: "/room/createRoom", number: category.CreateRoom },
        { name: "채팅방 목록", path: "/room/roomList", number: category.ChatList },
        { name: "채팅방 검색", path: "/chat/findRoom", number: category.findRoom },
    ]

    return (
        <>
            <div className="navbar__container">
                {navHeader.map((item, index) => {
                    return (
                        <Link to={item.path} state={{ selectedClass: [ item.number, item.name ] }} className="navbar__link" key={index}>
                            <div className="navbar__item">
                                {item.name}
                            </div>
                        </Link>
                    )
                })}
            </div>
        </>
    )
}

export default NavBar;