export default class RTC {
    private peerConnection!: RTCPeerConnection;
    private signaling;
    private listener;
    private ready = false;

    constructor(signaling: any, listener: any) {
        this.signaling = signaling;
        this.listener = listener;
        this.init();
    }

    init = () => {
        this.peerConnection = new RTCPeerConnection();
        this.peerConnection.addEventListener("icecandidate", (event) => {
            const message: any = {
                type: 'candidate',
                candidate: null,
            };

            if (event.candidate) {
                message.candidate = event.candidate.candidate;
                message.sdpMid = event.candidate.sdpMid;
                message.sdpMLineIndex = event.candidate.sdpMLineIndex;
            }

            this.signaling.postMessage(message);
        });

        this.peerConnection.addEventListener("track", (event) => {
            this.listener.onTrack(event);
        });

        this.listener.onReady(this.addTrack.bind(this));
        this.ready = true;
    }

    isReady() {
        return this.ready;
    }

    addTrack(track: MediaStreamTrack, ...stream:MediaStream[]) {
        this.peerConnection.addTrack(track, ...stream);
    }

    async createOffer(opts:any) {
        const offer = await this.peerConnection.createOffer(opts);
        this.signaling.postMessage({type: 'offer', sdp: offer.sdp});
        await this.peerConnection.setLocalDescription(offer);
    }

    async handleOffer(offer: RTCSessionDescriptionInit) {
        await this.peerConnection.setRemoteDescription(offer);
        const answer = await this.peerConnection.createAnswer();
        this.signaling.postMessage({type: 'answer', sdp: answer.sdp});
        await this.peerConnection.setLocalDescription(answer);
    }

    async handleAnswer(answer: RTCSessionDescriptionInit) {
        this.peerConnection.setRemoteDescription(answer);
    }

    async addIceCandidate(candidate: RTCIceCandidateInit | null) {
        await this.peerConnection.addIceCandidate(candidate?.candidate ? candidate : null);
    }

    close() {
        this.peerConnection.close();
        this.ready = false; 
        // this.peerConnection = null;
    }
}