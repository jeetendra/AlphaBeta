import { z } from "zod";
import { StructuredToolParams } from "@langchain/core/tools";
import { ChatOllama } from "@langchain/ollama";
import { HumanMessage, ToolMessage, SystemMessage } from "@langchain/core/messages";
import { tool } from "@langchain/core/tools";
import { ChatPromptTemplate } from "@langchain/core/prompts";
import { StringOutputParser } from "@langchain/core/output_parsers";
import { StructuredOutputParser } from "@langchain/core/output_parsers";

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

    const messages = [
        new SystemMessage("Give short answer only."),
        new HumanMessage("how is the weather in gurgaon haryana?")
    ];

    const aiMessage = await llmWithTools.invoke(messages);
    messages.push(aiMessage);
    if (aiMessage?.tool_calls) {
        for (const toolCall of aiMessage.tool_calls) {
            const selectedTool = toolsByName[toolCall.name];
            const toolMessage = await selectedTool.invoke(toolCall);
            messages.push(toolMessage);
        }
        // const answer = await llmWithTools.invoke(messages);
        // console.log(answer.content);

        const parser = new StringOutputParser();

        const jsonParser = StructuredOutputParser.fromNamesAndDescriptions(
            {
                name: "Name of the city",
                temperature: "Temperature of the city"
            }
        )

        const prompt = ChatPromptTemplate.fromMessages(messages);

        const answer = await prompt.pipe(llmWithTools)
            .pipe(parser)
            .invoke({
            });
        console.log(answer);
    }
}

main();