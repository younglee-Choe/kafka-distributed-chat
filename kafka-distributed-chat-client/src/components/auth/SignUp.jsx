import 'react-toastify/dist/ReactToastify.css';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { Button, Box, TextField } from '@mui/material';
import { ToastContainer } from 'react-toastify';

const SignUp = () => {
    const navigate = useNavigate();
    const [ userId, setUserId ] = useState('');
    const [ userPassword, setUserPassword ] = useState('');
    const [ userName, setUserName ] = useState('');

    const signupUrl = "/auth/signup";
    const signupData = {
        "memberId": userId,
        "password": userPassword,
        "name": userName
    };

    const onClickSignUp = () => {
        axios
            .post(signupUrl, signupData, 
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            )
            .then((res) => {
                console.log(res);
                navigate('/');
            })
            .catch(err => {
                console.log("An error occurred while logging in! ", err);
            });
    };

    return (
        <div className="home">
            <div className="home__container">
                <h2 className="home__header">Sign up to "Web-based Distributed Chat Service using Kafka"</h2>
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
                    <TextField 
                        id="username__input"
                        label="NICKNAME"
                        variant="outlined"
                        autoComplete="name"
                        name="name"
                        placeholder="NICKNAME"
                        type="text"
                        onChange={(e) => setUserName(e.target.value)}
                        value={userName}
                    />
                </Box>
                <Button
                    className="home__sign-up"
                    variant="contained"
                    onClick={() => { onClickSignUp(); }}
                >회원가입</Button>
                <ToastContainer
                    position="top-right"
                    autoClose={2000}
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
        </div>
    )
}

export default SignUp;