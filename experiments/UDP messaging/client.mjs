import dgram from "node:dgram";
import { Buffer } from "node:buffer";

const client = dgram.createSocket('udp4');

client.on("connect", () => {
    console.log("Connected");

    const message = Buffer.from("ping")

    client.send(message, 0, message.length, (err) => {
        if (err) {
            console.error('Error sending heartbeat:', err);
        } else {
            console.log('Heartbeat sent to server.');
        }
    });
});

client.on('error', (err) => {
    console.error('UDP Client error:', err);
    client.close();
});

client.on('message', (message, rinfo) => {
    console.log(`Server responded: ${message.toString()}`);
});

client.on('close', () => {
    console.log('UDP Client closed.');
});

client.connect(5000, "localhost");