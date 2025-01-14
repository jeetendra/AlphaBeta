import { chromaClient } from "./lib/chromadb";
import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm, embeddings } from "./lib/llm";
import { loadPDF, storeDataInDB, splitDocs } from "./lib/utils";


async function init() {
    const docs = await loadPDF("./extra/reactjs.pdf");
    const splittedDocs = await splitDocs(docs);

    const vectorStore = await storeDataInDB(splittedDocs, embeddings);
    return vectorStore;
}


async function main() {

    const vectorStore = init(); // TODO: find a solution to create vercor store without loading data.

    const retriever = vectorStore.asRetriever({
        k: 2
    });

    chromaClient.deleteCollection({
        name: "my_collection"
    });

    const question = "what are react's best practice";

    const results = await retriever.invoke(question);
    const resultDocs = results.map(
        result => result.pageContent
    );

    const template = ChatPromptTemplate.fromMessages([
        ['system', 'Answer the users question based on the following context: {context}'],
        ['user', '{input}']
    ]);

    const chain = template.pipe(llm);

    const response = await chain.invoke({
        input: question,
        context: resultDocs
    });

    console.log(response.content);

}

main();

