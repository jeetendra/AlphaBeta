import './style.css';
import SimplePeer from 'simple-peer';

document.querySelector<HTMLDivElement>('#app')!.innerHTML = `
  <div>
    Hello World
  </div>
`;

// const peer = new Peer();

// peer.on('open', (id) => {
//   console.log('My peer ID:', id);
//   // Send your ID to the server
//   const ws = new WebSocket('ws://localhost:3000');
//   ws.onopen = () => {
//     console.log("Sending open message")
//     ws.send(JSON.stringify({ type: 'id', id: id }));
//   };

//   ws.onmessage = (event) => {
//     const data = JSON.parse(event.data);
//     console.dir(data)
//     switch (data.type) {
//       case 'offer':
//         const call = peer.call(data.offer.id, stream);
//         call.on('stream', remoteStream => {
//           const remoteVideo = document.getElementById('remoteVideo');
//           remoteVideo.srcObject = remoteStream;
//         });
//         break;
//       case 'answer':
//         peer.on('call', call => {
//           call.answer(data.answer);
//         });
//         break;
//       case 'candidate':
//         peer.on('call', call => {
//           call.peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate));
//         });
//         break;
//     }
//   };
// });


let peer;
const socket = new WebSocket('wss://localhost:8081');

// peer = new SimplePeer({
//   initiator: location.hash === '#init',
//   trickle: false
// });

// if (peer.WEBRTC_SUPPORT) {
//   console.log("WebRTC is supported in this browser");
// } else {
//   console.log("WebRTC is not supported in this browser");
// }

navigator.mediaDevices.getUserMedia({ video: true, audio: false })
  .then(localStream => {
    const localVideo:HTMLVideoElement = document.getElementById('localVideo') as HTMLVideoElement ;
    localVideo.srcObject = localStream;

    peer = new SimplePeer({
      initiator: location.hash === '#init',
      trickle: false
    });

    if (peer.WEBRTC_SUPPORT) {
      console.log("WebRTC is supported in this browser");
    } else {
      console.log("WebRTC is not supported in this browser");
    }

    // peer.on('connect', () => {
    //   console.log('Connected to peer!');
    //   peer.send('Hello, World!');
    //   peer.addStream(localStream);
    // });

    // peer.on("data", (data) => {
    //   console.log("Received files and, messages from peer:", data);
    // });
    
    // peer.on("close", () => {
    //   console.log("Connection with peer closed :(");
    // });
    
    // peer.on("error", (err) => console.log("error", err));

    // peer.on("signal", (data) => {
    //   console.log("Hey! gotten signal data", data);
    // });

    peer.on('signal', data => {
      socket.send(data);
    });

    // Handle incoming stream
    // peer.on('stream', stream => {
    //     const remoteVideo = document.getElementById('remoteVideo') as HTMLVideoElement;
    //     remoteVideo.srcObject = stream;
    // });

    // Listen for signaling data from the server
    // socket.onmessage = (data) => {
    //     peer.signal(data);
    // };
  })
  .catch(err => console.error('Error accessing media devices.', err));