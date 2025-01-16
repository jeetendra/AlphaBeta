import { chromaClient } from "./lib/chromadb";
import { ChatPromptTemplate } from "@langchain/core/prompts";
import { llm, embeddings } from "./lib/llm";
import { loadPDF, storeDataInDB, splitDocs, getReteriver } from "./lib/utils";
import "dotenv/config";

async function loadDataInDb() {

    await chromaClient.deleteCollection({
        name: process.env.COLLECTION_NAME
    });
    const docs = await loadPDF("./extra/reactjs.pdf");
    const splittedDocs = await splitDocs(docs);

    await storeDataInDB(splittedDocs, embeddings);
}


async function main() {

    // await loadDataInDb();

    const retriever = await getReteriver(embeddings);

    const question = "explain use of useMemo and useCallback";

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

