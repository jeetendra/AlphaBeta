import {Tool} from 'ollama';

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
export function addNumbers({a, b}: {a:number, b: number}): number {
    return a + b;
}

type FunctionRef = {
    [key: string]: (...args: any[]) => any;
};

export const toolsRefs: FunctionRef = {
    "getCurrentTime": getCurrentTime,
    "searchWikipedia": searchWikipedia,
    "addNumbers": addNumbers
}

export const tools: Tool[] = [
  {
      type: "function",
      function: {
          name: "getCurrentTime",
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
          name: "searchWikipedia",
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
          name: "addNumbers",
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
];