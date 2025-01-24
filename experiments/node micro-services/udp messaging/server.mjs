import dgram from "node:dgram";
import { Buffer } from "node:buffer";

const server = dgram.createSocket({ type: 'udp4' });

server.on("message", (msg, rinfo) => {
    console.log(`server got: ${msg} from ${rinfo.address}:${rinfo.port}`)
});

server.on('listening', () => {
    const address = server.address();
    console.log(`Server listening on ${address.address}:${address.port}`);
  });

  server.bind(5000);
