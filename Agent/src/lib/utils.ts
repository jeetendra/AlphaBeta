import { PDFLoader } from "@langchain/community/document_loaders/fs/pdf";
import { RecursiveCharacterTextSplitter } from "@langchain/textsplitters";
import { Chroma } from "@langchain/community/vectorstores/chroma"

export async function loadPDF(file: string) {
    const loader = new PDFLoader("./extra/reactjs.pdf", {
        splitPages: false
    });

    const docs = await loader.load();

    return docs;
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
    const vectorStore = await Chroma.fromDocuments(splittedDocs, embeddings, {
        collectionName: "react",
        url: process.env.CHROMA_URI
    });

    await vectorStore.addDocuments(splittedDocs);
    return vectorStore;
}