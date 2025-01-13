import ollama from 'ollama';
import * as readline from 'node:readline/promises';
import { stdin as input, stdout as output } from 'node:process';
import { getCurrentTime, searchWikipedia, addNumbers } from './tools';

type FunctionRef = {
    [key: string]: (...args: any[]) => any;
};
var functions: FunctionRef = {
    "getCurrentTime": getCurrentTime,
    "searchWikipedia": searchWikipedia,
    "addNumbers": addNumbers
}

interface Tool {
    name: string; 
    arguments: string[]
}

const snakeToCamel = (str: string) =>
    str.toLowerCase().replace(/([-_][a-z])/g, group =>
      group
        .toUpperCase()
        .replace('-', '')
        .replace('_', '')
    );

async function runAgent(model: string): Promise<void> {
  const rl = readline.createInterface({ input, output });
  let conversationHistory: { role: 'user' | 'assistant'; content: string }[] = [];

  console.log(`Starting conversation with ${model}. Type 'exit' to quit.`);

  while (true) {
    const userInput = await rl.question('You: ');

    if (userInput.toLowerCase() === 'exit') {
      break;
    }

    conversationHistory.push({ role: 'user', content: userInput });

    try {
      const response = await ollama.chat({
        model: model,
        messages: conversationHistory,
        tools: [
            {
                type: "function",
                function: {
                    name: "get_current_time",
                    description: "Gets the current time.",
                    parameters: {
                        type: "object",
                        required: [],
                        properties: {}
                    }
                }
            },
            {
                type: "function",
                function: {
                    name: "search_wikipedia",
                    description: "Searches Wikipedia for a given query.",
                    parameters: {
                        type: "object",
                        required: ["query"],
                        properties: {
                            query: {
                                type: "string",
                                description: "The search query.",
                            },
                        },
                    }
                }
            },
            {
                type: "function",
                function: {
                    name: "add_numbers",
                    description: "Adds two numbers",
                    parameters: {
                        type: "object",
                        required: ["a", "b"],
                        properties: {
                            a: {
                                type: "number",
                                description: "The first number."
                            },
                            b: {
                                type: "number",
                                description: "The second number."
                            }
                        }
                    }
                }
            }
        ]
      });

      if (response?.message?.content) {
        console.log(`Agent: ${response.message.content}`);
        conversationHistory.push({ role: 'assistant', content: response.message.content });
      } else if(response["message"]["tool_calls"]) {
        const tools = response["message"]["tool_calls"][0]["function"] as Tool;
        const functionName: string = snakeToCamel(tools["name"]);
        
        const funct = functions[functionName];
        console.log(funct);

        var val = await funct.apply(null, tools["arguments"]);
        console.log({val});
        console.log(tools["arguments"]);
      } else {
        console.error("Unexpected response format:", response);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }

  rl.close();
}

async function main(): Promise<void> {
  const modelName = process.argv[2] || 'llama3.2'; // Get model name from command line or default to llama2
  console.log(`Using model: ${modelName}`);
  await runAgent(modelName);
}

main();
