import { createServer } from 'https';
import WebSocket, { WebSocketServer } from 'ws';
import { readFileSync } from 'fs';



const server = createServer({
    cert: readFileSync('../cert/cert.pem'),
    key: readFileSync('../cert/key.pem')
});

const wss = new WebSocketServer({ server });

wss.on('connection', function connection(ws) {
    
    ws.on('error', console.error);

    ws.on('message', (data, isBinary) => {
        
        wss.clients.forEach((client) => {
            if (client !== ws && client.readyState === WebSocket.OPEN) {

                // if(isBinary) {
                //     const modifiedData = new Uint8Array(data); 
                //     for (let i = 0; i < modifiedData.length; i++) {
                //         modifiedData[i] += 1; 
                //     }
                //     data = modifiedData;
                // }
                client.send(data, {
                    binary: isBinary
                });
            }
        });
    });
});

server.listen(8081);