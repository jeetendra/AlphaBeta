import ollama from 'ollama';

async function main() {
  try {
    const response = await ollama.chat({
      model: 'llama3.2', // Replace with your model name
      messages: [{ role: 'user', content: 'What is the capital of France?' }],
    });

    console.log(response.message.content);
  } catch (error) {
    console.error('Error:', error);
  }
}

main();