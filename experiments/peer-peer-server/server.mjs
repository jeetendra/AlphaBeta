import { createServer } from 'https';
import WebSocket, { WebSocketServer } from 'ws';
import { readFileSync } from 'fs';
import { v4 as uuidv4 } from 'uuid';

const server = createServer({
    cert: readFileSync('../cert/cert.pem'),
    key: readFileSync('../cert/key.pem')
});

const wss = new WebSocketServer({ server });

// wss.on('connection', function connection(ws) {
//     ws.on('error', console.error);
//     ws.on('message', (data, isBinary) => {
//         wss.clients.forEach((client) => {
//             if (client !== ws && client.readyState === WebSocket.OPEN) {
//                 client.send(data, {
//                     binary: isBinary
//                 });
//             }
//         });
//     });
// });


wss.on('connection', (ws) => {
    const id = uuidv4(); 
    console.log("Connected");
    ws.on('message', (message) => {
        const data = JSON.parse(message);
        console.log(data);
        switch (data.type) {
            case 'offer':
                wss.clients.forEach((client) => {
                    if (client !== ws && client.readyState === WebSocket.OPEN) {
                        client.send(JSON.stringify({ type: 'offer', offer: data.offer }));
                    }
                });
                break;
            case 'answer':
                wss.clients.forEach((client) => {
                    if (client !== ws && client.readyState === WebSocket.OPEN) {
                        client.send(JSON.stringify({ type: 'answer', answer: data.answer }));
                    }
                });
                break;
            case 'candidate':
                wss.clients.forEach((client) => {
                    if (client !== ws && client.readyState === WebSocket.OPEN) {
                        client.send(JSON.stringify({ type: 'candidate', candidate: data.candidate }));
                    }
                });
                break;
        }
    });
});

server.listen(8081);