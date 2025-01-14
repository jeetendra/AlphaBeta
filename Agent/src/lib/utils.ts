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
    const vectorStore = await Chroma.fromDocuments(splittedDocs, embeddings, {
        collectionName: process.env.COLLECTION_NAME,
        url: process.env.CHROMA_URI
    });
}

export async function getReteriver(embeddings) {
    var chroma = await Chroma.fromExistingCollection(embeddings,{
        collectionName: process.env.COLLECTION_NAME,
        url: process.env.CHROMA_URI
    });

    return chroma.asRetriever({
        k: 2
    });
}