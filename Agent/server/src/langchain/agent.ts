import { ChatOpenAI } from "@langchain/openai";
import { tool } from "@langchain/core/tools";
import { number, z } from "zod";
import { ChatPromptTemplate } from "@langchain/core/prompts";
import { createToolCallingAgent } from "langchain/agents";
import { AgentExecutor } from "langchain/agents";

const llm = new ChatOpenAI({
    model: "llama3.2",
    openAIApiKey: "ollama",
    configuration: {
        baseURL: "http://127.0.0.1:11434/v1/",
    }
});

const addTool = tool(
    // Intentionally pass value of (a and b) as a string, otherwise llm is throwing error
    async ({a, b}) => {
        console.log("TOOL CALLED",a,b);
        return `${(parseInt(a) + parseInt(b))}`;
    },
    {
        name: "adder_function",
        description: "Adds two numbers together",
        schema: z.object({
            a: z.string().describe("The first number"),
            b: z.string().describe("The second number"),
        }),
    }
);  

const magicTool = tool(
    async ({ input }: { input: number }) => {
        return `${input + 2}`;
    },
    {
        name: "magic_function",
        description: "Applies a magic function to an input.",
        schema: z.object({
            input: z.number(),
        }),
    }
);



const tools = [addTool];

const query = "what is the sum of 11 and 201";


const prompt = ChatPromptTemplate.fromMessages([
    ["system", "You are a helpful assistant"],
    ["placeholder", "{chat_history}"],
    ["human", "{input}"],
    ["placeholder", "{agent_scratchpad}"],
  ]);
  
  const agent = createToolCallingAgent({
    llm,
    tools,
    prompt,
  });
  const agentExecutor = new AgentExecutor({
    agent,
    tools,
  });
(async () => {
    const answer = await agentExecutor.invoke({ input: query });
    console.dir(answer);
})();

