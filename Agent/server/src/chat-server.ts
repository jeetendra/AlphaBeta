import express, { Request, Response, NextFunction } from "express";
import cors from "cors";
import bodyParser from "body-parser";
import 'dotenv/config';
import { HumanMessage, SystemMessage } from "@langchain/core/messages";
import { ChatOllama } from "@langchain/ollama";


const port = 8001;

const app = express();

app.use(cors());

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

    const messages = [
        new SystemMessage("Give short answer only."),
        new HumanMessage(prompt)
    ];

    const stream = await llm.stream(messages);

    const headers = {
        'Content-Type': 'text/event-stream',
        'Connection': 'keep-alive',
        'Cache-Control': 'no-cache'
    };

    res.writeHead(200, headers);

    for await (const chunk of stream) {
        console.log(chunk.content);
        res.write(`data: ${chunk.content}`);
        await sleep(20);
    }

    res.end();
}


app.get("/chat", chat);

app.listen(port, () => {
    console.log(`Now listening on port ${port}`);
});
