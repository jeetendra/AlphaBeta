import { ChromaClient } from "chromadb";
import { Chroma } from '@langchain/community/vectorstores/chroma';
export const chromaClient = new ChromaClient({
    path: "http://localhost:8000",
    // auth: {
    //     provider: "token",
    //     credentials: process.env.CHROMA_CLIENT_AUTH_CREDENTIALS,
    //     tokenHeaderType: process.env.CHROMA_AUTH_TOKEN_TRANSPORT_HEADER
    // }
})

// async function main() {
//     const data = await chromaClient.heartbeat();

//     console.log(data);    
// }

// main();
