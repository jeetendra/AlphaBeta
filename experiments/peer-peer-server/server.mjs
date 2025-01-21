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

const clients = {};


wss.on('connection', (ws) => {
    const id = uuidv4(); 
    clients[id] = ws;
    console.log("Connected");
    ws.send(JSON.stringify({ type: 'id', id: id }));

    ws.on('message', (message) => {
        const data = JSON.parse(message);
        console.log(data);
        switch (data.type) {
            case 'offer':
                {
                    const toId = data.to;
                    const offer = data.offer;

                    if (clients[toId]) {
                        clients[toId].send(JSON.stringify({ type: 'offer', from: id, offer: offer }));
                    } else {
                        console.log('User not found.');
                    }
                }
                // wss.clients.forEach((client) => {
                //     if (client !== ws && client.readyState === WebSocket.OPEN) {
                //         client.send(JSON.stringify({ type: 'offer', offer: data.offer }));
                //     }
                // });
                break;
            case 'answer':
                {
                    const toId = data.to;
                    const answer = data.answer;

                    if (clients[toId]) {
                        clients[toId].send(JSON.stringify({ type: 'answer', from: id, answer: answer }));
                    } else {
                        console.log('User not found.');
                    }
                }
                // wss.clients.forEach((client) => {
                //     if (client !== ws && client.readyState === WebSocket.OPEN) {
                //         client.send(JSON.stringify({ type: 'answer', answer: data.answer }));
                //     }
                // });
                break;
            case 'candidate':
                {
                    const toId = data.to;
                    const candidate = data.candidate;

                    if (clients[toId]) {
                        clients[toId].send(JSON.stringify({ type: 'candidate', from: id, candidate: candidate }));
                    } else {
                        console.log('User not found.');
                    }
                }
                // wss.clients.forEach((client) => {
                //     if (client !== ws && client.readyState === WebSocket.OPEN) {
                //         client.send(JSON.stringify({ type: 'candidate', candidate: data.candidate }));
                //     }
                // });
                break;
        }
    });

    ws.on('close', () => {
        delete clients[id];
        console.log('Client disconnected with ID:', id);
    });
});

server.listen(8081);