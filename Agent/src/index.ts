import { z } from "zod";
import { StructuredToolParams } from "@langchain/core/tools";
import { ChatOllama } from "@langchain/ollama";
import { HumanMessage, ToolMessage } from "@langchain/core/messages";
import { tool } from "@langchain/core/tools";

const weatherToolSchema: StructuredToolParams = {
    name: "getCurrentWeather",
    description: "Get the current weather for a location",
    schema: z.object({
        city: z.string().describe("The city to get the weather for"),
        state: z.string().optional().describe("The state to get the weather for"),
    }),
};

const weatherToolFn = async () => {
    return "its 8 degree c";
}

const weatherTool = tool(
    weatherToolFn,
    weatherToolSchema
);

const toolsByName = {
    "getCurrentWeather": weatherTool
};

const llm = new ChatOllama({
  model: "llama3.2",
  temperature: 0,
  maxRetries: 2,
});

const llmWithTools = llm.bindTools([weatherTool]);

async function main() {
    
    const messages = [new HumanMessage("how is the weather in gurgaon haryana?")];

    const aiMessage = await llmWithTools.invoke(messages);
    console.log(aiMessage);

    messages.push(aiMessage);
    if(aiMessage?.tool_calls) {
        for (const toolCall of aiMessage.tool_calls) {
            const selectedTool = toolsByName[toolCall.name];
            const toolMessage = await selectedTool.invoke(toolCall);
            // const toolMessage = new ToolMessage({
            //     tool_call_id: toolCall.id,
            //     status: "success",
            //     content: "its 10 degree c"
            // })
            messages.push(toolMessage);
        }
        const answer = await llmWithTools.invoke(messages);
        console.log("##")
        console.log(answer);
    }
    
}

main();