let ws;
let username;

document.getElementById('username').addEventListener('change', (event) => {
    username = event.target.value;
    ws = new WebSocket('ws://localhost:8080/ws/chat');

    ws.onmessage = (event) => {
        const output = document.getElementById('output');
        output.innerHTML += `<p>${event.data}</p>`;
        output.scrollTop = output.scrollHeight;
    };
});

function sendMessage() {
    const messageInput = document.getElementById('message');
    const message = messageInput.value;
    if (message && ws) {
        ws.send(`${username}: ${message}`);
        messageInput.value = '';
    }
}