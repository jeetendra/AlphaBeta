import OpenAI from 'openai'

const openai = new OpenAI({
    baseURL: "http://127.0.0.1:11434/v1/",
    apiKey: 'ollama',
});

async function main() {
    const embedding = await openai.embeddings.create({
        model: "nomic-embed-text",
        input: ["why is the sky blue?", "why is the grass green?"],
    });
    console.log(embedding);

    // const completion = await openai.completions.create({
    //     model: "llama3.2",
    //     prompt: "Say this is a test.",
    // });
    // console.log(completion);

    // const chatCompletion = await openai.chat.completions.create({
    //     messages: [{ role: 'user', content: 'Say this is a test' }],
    //     model: 'llama3.2',
    // });
    // console.log(chatCompletion);
}

main();
