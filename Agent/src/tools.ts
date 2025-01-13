interface WikipediaResponse {
    query: {
        pages: {
            [key: string]: {
                extract?: string;
                missing?: boolean;
            };
        };
    };
}


// tools.ts (or a similar file)
export async function getCurrentTime(): Promise<string> {
    return new Date().toLocaleString();
}

export async function searchWikipedia(query: string): Promise<string> {
    try {
        const response = await fetch(`https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&titles=${query}&exintro&explaintext`);
        const data: unknown = await response.json();
        const page = Object.values((data as WikipediaResponse).query.pages)[0] as WikipediaResponse['query']['pages'][string]; // Type assertions
        if (page.extract) {
            return page.extract.slice(0, 500) + "..."; // Limit the extract length
        } else if (page.missing) {
            return `No Wikipedia page found for "${query}".`;
        }
        return "Could not retrieve data from Wikipedia.";
    } catch (error) {
        console.error("Error searching Wikipedia:", error);
        return "Error searching Wikipedia.";
    }
}

// Example of a synchronous tool
export function addNumbers(a: number, b: number): number {
    return a + b;
}