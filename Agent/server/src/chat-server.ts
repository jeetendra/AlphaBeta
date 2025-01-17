import express, { Request, Response, NextFunction } from "express";
import cors from "cors";
import bodyParser from "body-parser";
// import { BrotliCompress } from "zlib";

import Compression from "compression";

import 'dotenv/config';
import { HumanMessage, SystemMessage } from "@langchain/core/messages";
import { ChatOllama } from "@langchain/ollama";
import SSE from 'express-sse';
var sse = new SSE();

const port = 8001;

const app = express();

app.use(cors());
app.use(Compression());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

const llm = new ChatOllama({
    model: "llama3.2",
    temperature: 0,
    maxRetries: 2,
    streaming: true
});

async function sleep(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function chat(req: Request, res: Response, next: NextFunction) {
    console.log("API_CALL");
    const prompt = req.query.prompt as string;
    res.sendStatus(200); // return early no need to wait for chat response
    const messages = [
        new SystemMessage("Give short answer only."),
        new HumanMessage(prompt)
    ];

    const stream = await llm.stream(messages);

    for await (const chunk of stream) {
        console.log(chunk.content);
        // if(chunk.content)
        sse.send({content: chunk.content}); 
        await sleep(20);
    }
    sse.send({content: "\n"}); 
}

app.get("/chat", chat);
app.get('/stream',(req, res, next) => {
    // work-around for flush error by expess-sse
    // res["flush"] = () => {}; 
    next();
  }, sse.init);

app.listen(port, () => {
    console.log(`Now listening on port ${port}`);
});
