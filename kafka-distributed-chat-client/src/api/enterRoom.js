import axios from "axios";

function enterRoom(roomId, isReEnter) {
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

    if (isReEnter && replacePathname !== roomId) {
        window.location.href = `/sub/chat/${roomId}`;
    } else if(replacePathname !== roomId) {
        axios.post(enterRoomUrl, enterRoomData, {
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then((res) => {
            if (res.data !== "error") {
                window.location.href = `/sub/chat/${roomId}`;
            }
        })
        .catch((err) => console.log("An error occurred while entering the chat room! ", err));
    }
};

export default enterRoom; 