import { useEffect, useState } from "react"

function App() {
  
  const [query, setQuery] = useState("");
  const [submit, setSubmit] = useState(false);
  const [ response, setResponse ] = useState<string>('>>>>>');

  useEffect(() => {
    if (submit && query.length) {
      console.log("Query submitted:", query);
      
      const eventSource = new EventSource('http://localhost:8001/chat?prompt=' + encodeURIComponent(query))

      // Subscribe to all events streaming in
      eventSource.onmessage = (event) => {
        console.log(">>",event.data.trim());
        if(event.data.trim() !== 'undefined'){
          const newData = event.data;
          setResponse((prevResponse) => prevResponse.concat(newData));
        } else{
          // close the SSE connection if the server sends an event message with data 'undefined'
          eventSource.close();
          
        }
      };
      setQuery("");
      setSubmit(false);
    }
    
  }, [submit, query]);

  const triggerQuery = () => {
    setSubmit(true);
  };

  return (
    <>
      <div>{response}</div>
      
      <input 
        value={query} 
        onChange={(e) => setQuery(e.target.value)} 
      /> 
      <button onClick={triggerQuery}>Submit</button>
    </>
  )
}

export default App
