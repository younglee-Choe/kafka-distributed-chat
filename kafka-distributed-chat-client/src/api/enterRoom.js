import axios from "axios";

function enterRoom(roomId) {
    const memberId = sessionStorage.getItem("memberId");
    const memberName = sessionStorage.getItem("memberName");
    const date = new Date();

    const pathname = window.location.pathname;
    const replacePathname = pathname.replace("/sub/chat/", "");
    
    const enterRoomUrl = "/room/enter";
    const enterRoomData = {
        roomId: roomId, 
        memberId: memberId,
        memberName: memberName,
        date: date
    };

    axios.post(enterRoomUrl, enterRoomData, {
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then((res) => {
        if (replacePathname !== roomId) {
            window.location.href = `/sub/chat/${roomId}`;
        }
    })
    .catch((err) => console.log("An error occurred while searching or entering the room! ", err));
};

export default enterRoom;