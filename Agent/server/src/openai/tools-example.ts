import { ChatOpenAI } from "@langchain/openai";
import { tool } from "@langchain/core/tools";
import { z } from "zod";

const weatherTool = tool((_) => "no-op", {
    name: "get_current_weather",
    description: "Get the current weather",
    schema: z.object({
        location: z.string(),
    }),
});

const searchTool = tool((_) => "no-op", {
    name: "get_web_search",
    description: "Search the web for latest news",
    schema: z.object({
        searchTerm: z.string(),
    }),
});

const dateTimeTool = tool((_) => "no-op", {
    name: "get_date_time",
    description: "get current date and/or time",
    schema: z.object({
        searchTerm: z.string(),
    }),
});

const llm = new ChatOpenAI({
    model: "llama3.2",
    openAIApiKey: "ollama",
    configuration: {
        baseURL: "http://127.0.0.1:11434/v1/",
    }
}).bindTools([weatherTool, searchTool, dateTimeTool]);


async function main() {
    const result = await llm.invoke(
        "what date is today "
    );

    console.dir(result.tool_calls, { depth: null });
}

main();