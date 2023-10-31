import '../../css/Chat.css';
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const ChatBar = () => {
  const [ users, setUsers ] = useState([]);

  useEffect(() => {
    // axios.get("/chat")
    //   .then((response) => console.log("chat; ", response))
    //   // .then((json) => setMessage(json.SUCCESS_TEXT))
    //   .catch(error => console.log("error: ", error));
    setUsers(sessionStorage.getItem("username"));
    console.log("users: ", users);
  }, [users]);

  return (
    <div className="chat__sidebar">
      <h2>Room Name</h2>
      <div>
        <h4 className="chat__header">ACTIVE USERS</h4>
        <div className="chat__users">
          {users.length > 0 ? 
            (
              // users.map((user) => 
              //   <p key={user.socketID}>{user.userName}</p>
              // )
              users
            ) : (
              <div>No active users</div>
            )
          }
        </div>
      </div>
    </div>
  )
}

export default ChatBar;