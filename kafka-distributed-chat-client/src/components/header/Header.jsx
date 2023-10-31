import "../../css/Header.css";
import React from "react";
import { useNavigate } from 'react-router-dom';
import { Button } from '@mui/material';
import NavBar from "./NavBar";

function Header() {
    const navigate = useNavigate();
    const onClickLogout = () => {
        sessionStorage.removeItem("memberId");
        navigate("/");
    }

    return (
        <>
            <div className="header__container">
                <NavBar/>
                <Button
                    className="header-log-out__btn"
                    variant="contained"
                    onClick={() => { onClickLogout(); }}
                >로그아웃</Button>
            </div>
        </>
    )
}

export default Header;