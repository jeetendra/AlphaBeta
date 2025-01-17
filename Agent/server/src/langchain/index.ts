import { OpenAIEmbeddings } from "@langchain/openai";
import { PDFLoader } from "@langchain/community/document_loaders/fs/pdf";
import { RecursiveCharacterTextSplitter } from "@langchain/textsplitters";
import { Chroma } from "@langchain/community/vectorstores/chroma"

export async function loadPDF(file: string) {
    const loader = new PDFLoader(file, {
        splitPages: false
    });

    return await loader.load();
}

export async function splitDocs(docs) {
    const splitter = new RecursiveCharacterTextSplitter({
        separators: [`. \n`],
        chunkSize: 300,
        chunkOverlap: 10,
    });
    return splitter.splitDocuments(docs);
}

export async function storeDataInDB(splittedDocs, embeddings) {

    const vectorStore = new Chroma(embeddings, {
        collectionName: "langa",
    });

    await vectorStore.addDocuments(splittedDocs);

    // const vectorStore = await Chroma.fromDocuments(splittedDocs, embeddings, {
    //     collectionName: process.env.COLLECTION_NAME,
    //     url: process.env.CHROMA_URI
    // });
}


(async () => {

    const embeddings = new OpenAIEmbeddings({
        model: "nomic-embed-text",
        openAIApiKey: "ollama",
        configuration: {
            baseURL: "http://127.0.0.1:11434/v1/",
        }
    });
    
    const docs = await loadPDF("./extra/reactjs.pdf");
    const splittedDocs = await splitDocs(docs);
    await storeDataInDB(splittedDocs, embeddings);


    const vectorStore = new Chroma(embeddings, {
        collectionName: "langa",
    });
    const data = await vectorStore.similaritySearch("useState", 1);

    console.dir(data);

    
    const query = await embeddings.embedQuery("useState");
    console.dir(query);
    const res = await vectorStore.similaritySearchVectorWithScore(query, 1);
    console.dir(res);

})();

