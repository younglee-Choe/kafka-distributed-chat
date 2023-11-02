import '../../css/Auth.css';
import 'react-toastify/dist/ReactToastify.css';
import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Button, Box, TextField } from '@mui/material';
import { ToastContainer, toast } from 'react-toastify';

const LogIn = () => {
    const navigate = useNavigate();    
    const [ userId, setUserId ] = useState('');
    const [ userPassword, setUserPassword ] = useState('');
    const [ isSession, setSession ] = useState(true);

    const memberId = sessionStorage.getItem("memberId");

    const loginUrl = "/auth/login";
    const loginData = {
        "memberId": userId,
        "password": userPassword
    };

    const onClickLogin = () => {
        axios.post(loginUrl, loginData, 
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            )
            .then((res) => {
                console.log(res);

                if(res.data.memberId === userId) {
                    console.log("로그인 성공");
                    sessionStorage.setItem("memberId", res.data.memberId);
                    sessionStorage.setItem("memberName", res.data.memberName);
                    setSession(true);
                    navigate('/room/roomList');
                    window.location.reload();
                } else {
                    toast.error(<><h4>로그인 실패</h4>아이디 또는 비밀번호를 다시 입력해주세요</>)
                }
            })
            .catch(err => {
                console.log("An error occurred while logging in! ", err);
            });
    };

    const onClickSignUp = () => {
        navigate('/signup');
    };

    useEffect(() => {
        if(memberId === null) {
            setSession(false);
        }
    }, [memberId])

    return (
        <div className="home">
            {isSession ?
                <div className="home__container--logged-in">
                    이미 로그인됨
                    {/* <ChatList /> */}
                </div>
            :
                <div className="home__container">
                    <h2 className="home__header">Log in to "Web-based Distributed Chat Service using Kafka"</h2>
                    <Box
                        component="form"
                        sx={{
                            '& > :not(style)': { m: 1, width: '25ch' },
                        }}
                        noValidate
                        autoComplete="off"
                    >
                        <TextField 
                            id="userid__input"
                            label="ID"
                            variant="outlined"
                            autoComplete="id"
                            name="id"
                            placeholder="ID"
                            onChange={(e) => setUserId(e.target.value)}
                            value={userId}
                        />
                        <TextField 
                            id="userpassword__input"
                            label="PASSWORD"
                            variant="outlined"
                            autoComplete="new-password"
                            name="password"
                            placeholder="PASSWORD"
                            type="password"
                            onChange={(e) => setUserPassword(e.target.value)}
                            value={userPassword}
                        />
                    </Box>
                    <div className="home__btn">
                        <Button
                            className="home-log-in__btn"
                            variant="contained"
                            onClick={() => { onClickLogin(); }}
                        >로그인</Button>
                        <Button
                            className="home-sign-up__btn"
                            variant="contained"
                            onClick={() => { onClickSignUp(); }}
                        >회원가입</Button>
                    </div>
                    <ToastContainer
                        className="login-fail__toast"
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
            }
        </div>
    )
}

export default LogIn;