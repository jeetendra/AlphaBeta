import { ChatOllama } from "@langchain/ollama";
import { OllamaEmbeddings } from "@langchain/ollama";

export const llm = new ChatOllama({
    model: "llama3.2",
    temperature: 0,
    maxRetries: 2,
});

export const embeddings = new OllamaEmbeddings({
    model: "nomic-embed-text",
    // baseUrl: "http://localhost:11434",
});