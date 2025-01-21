import './style.css';
import RTC from './rtc.ts';

document.querySelector<HTMLDivElement>('#app')!.innerHTML = `
  <div>
    Hello World
  </div>
`;

// const signaling: any = {
//   postMessage() {

//   },
// };
const startButton = document.getElementById('startButton');
const hangupButton = document.getElementById('hangupButton');
hangupButton.disabled = true;

const localVideo = document.getElementById('localVideo');
const remoteVideo = document.getElementById('remoteVideo');

// const signaling = new BroadcastChannel('webrtc');
const signaling = new WebSocket('wss://localhost:8081');
var clientId;
signaling.onmessage = e => {
  if (!localStream) {
    console.log('not ready yet');
    return;
  }
  switch (e.data.type) {
    case 'id':
      clientId = e.data.id;
      break;
    case 'offer':
      handleOffer(e.data);
      break;
    case 'answer':
      handleAnswer(e.data);
      break;
    case 'candidate':
      handleCandidate(e.data);
      break;
    case 'ready':
      // A second tab joined. This tab will initiate a call unless in a call already.
      if (rtc?.isReady()) {
        console.log('already in call, ignoring');
        return;
      }
      makeCall();
      break;
    case 'bye':
      if (rtc?.isReady()) {
        hangup();
      }
      break;
    default:
      console.log('unhandled', e);
      break;
  }
};

const listener: any = {
  onTrack(e) {
    remoteVideo.srcObject = e.streams[0]
  },
  onReady(addTrack) {
    localStream.getTracks()
      .forEach(
        track => addTrack(track, localStream)
      );
  }
};

let rtc;
let localStream;

function createPeerConnection() {
  rtc = new RTC(signaling, listener)
}

async function makeCall() {
  createPeerConnection();
  await rtc.createOffer();
}

async function handleOffer(offer) {
  if (rtc) {
    console.error('existing peerconnection');
    return;
  }
  createPeerConnection();
  await rtc.handleOffer(offer);
}

async function handleAnswer(answer) {
  if (!rtc) {
    console.error('no peerconnection');
    return;
  }
  await rtc.handleAnswer(answer);
}

async function handleCandidate(candidate) {
  if (!rtc) {
    console.error('no peerconnection');
    return;
  }
  rtc.addIceCandidate(candidate)
}

async function hangup() {
  if (rtc) {
    rtc.close();
    rtc = null;
  }
  localStream.getTracks().forEach(track => track.stop());
  localStream = null;
  startButton.disabled = false;
  hangupButton.disabled = true;
};

startButton.onclick = async () => {
  localStream = await navigator.mediaDevices.getUserMedia({audio: true, video: true});
  localVideo.srcObject = localStream;


  startButton.disabled = true;
  hangupButton.disabled = false;

  signaling.postMessage({type: 'ready'});
};

hangupButton.onclick = async () => {
  hangup();
  signaling.postMessage({type: 'bye'});
};


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


// let peer;
// const socket = new WebSocket('wss://localhost:8081');

// peer = new SimplePeer({
//   initiator: location.hash === '#init',
//   trickle: false
// });

// if (peer.WEBRTC_SUPPORT) {
//   console.log("WebRTC is supported in this browser");
// } else {
//   console.log("WebRTC is not supported in this browser");
// }

// navigator.mediaDevices.getUserMedia({ video: true, audio: false })
//   .then(localStream => {
//     const localVideo:HTMLVideoElement = document.getElementById('localVideo') as HTMLVideoElement ;
//     localVideo.srcObject = localStream;

//     peer = new SimplePeer({
//       initiator: location.hash === '#init',
//       trickle: false
//     });

//     if (peer.WEBRTC_SUPPORT) {
//       console.log("WebRTC is supported in this browser");
//     } else {
//       console.log("WebRTC is not supported in this browser");
//     }

//     // peer.on('connect', () => {
//     //   console.log('Connected to peer!');
//     //   peer.send('Hello, World!');
//     //   peer.addStream(localStream);
//     // });

//     // peer.on("data", (data) => {
//     //   console.log("Received files and, messages from peer:", data);
//     // });
    
//     // peer.on("close", () => {
//     //   console.log("Connection with peer closed :(");
//     // });
    
//     // peer.on("error", (err) => console.log("error", err));

//     // peer.on("signal", (data) => {
//     //   console.log("Hey! gotten signal data", data);
//     // });

//     peer.on('signal', data => {
//       socket.send(data);
//     });

//     // Handle incoming stream
//     // peer.on('stream', stream => {
//     //     const remoteVideo = document.getElementById('remoteVideo') as HTMLVideoElement;
//     //     remoteVideo.srcObject = stream;
//     // });

//     // Listen for signaling data from the server
//     // socket.onmessage = (data) => {
//     //     peer.signal(data);
//     // };
//   })
//   .catch(err => console.error('Error accessing media devices.', err));