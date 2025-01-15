import { ChatOpenAI } from "@langchain/openai";

const llm = new ChatOpenAI({
    model: "llama3.2",
    openAIApiKey: "ollama",
    configuration: {
        baseURL: "http://127.0.0.1:11434/v1/",
    }
});

async function main() {
    const messages = [{ role: 'user', content: 'What is capital of USA' }];
    const completion = await llm.invoke(messages);
    console.log(completion.content);
}

main();
