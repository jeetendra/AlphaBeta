import ollama, {ChatResponse} from 'ollama';
import * as readline from 'node:readline/promises';
import { stdin as input, stdout as output } from 'node:process';
import { toolsRefs, tools} from './tools';


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
      const response: ChatResponse = await ollama.chat({
        model: model,
        messages: conversationHistory,
        tools: tools
      });

      if (response?.message?.content) {
        console.log(`Agent: ${response.message.content}`);
        conversationHistory.push({ role: 'assistant', content: response.message.content });
      } else if(response["message"]["tool_calls"]) {
        const toolCall = response["message"]["tool_calls"][0];

        
        console.log(toolCall.function.name);
        const funct = toolsRefs[toolCall.function.name];
        
        var val = await funct.call(null, toolCall.function.arguments);
        console.log({val});
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
