import { useEffect, useState } from "react"
import Markdown from "react-markdown";
import { atomDark } from 'react-syntax-highlighter/dist/esm/styles/prism'
import {Prism, SyntaxHighlighterProps} from 'react-syntax-highlighter';
const SyntaxHighlighter = Prism as any as React.FC<SyntaxHighlighterProps>; // workaround to make it work

function App() {

  const [query, setQuery] = useState("");
  const [submit, setSubmit] = useState(false);
  const [response, setResponse] = useState<string>('');


  useEffect(() => {
    let eventSource: EventSource;

    eventSource = new EventSource('http://localhost:8001/stream')

    // Subscribe to all events streaming in
    eventSource.onmessage = (event) => {
      console.log(">>", event.data.trim());
      if (event.data.trim() !== '') {
        const newData = JSON.parse(event.data);
        setResponse((prevResponse) => prevResponse + newData.content);
      } else {
        // close the SSE connection if the server sends an event message with data 'undefined'
        eventSource.close();
      }
    };
    return () => {
      console.log("close connection")
      eventSource?.close();
    };
  }, []);

  useEffect(() => {
    if (submit && query.length) {
      console.log("Query submitted:", query);
      fetch('http://localhost:8001/chat?prompt=' + encodeURIComponent(query))
        .then(_ => console.log("Done"));
      setQuery("");
      setSubmit(false);
    }
  }, [submit, query]);

  const triggerQuery = () => {
    setSubmit(true);
  };

  return (
    <>
      <Markdown
        children={response}
        components={{
          code(props) {
            const { children, className, node, ...rest } = props
            const match = /language-(\w+)/.exec(className || '')
            return match ? (
              <SyntaxHighlighter
                {...(rest as any)}
                PreTag="div"
                children={String(children).replace(/\n$/, '')}
                language={match[1]}
                style={atomDark}
              ></SyntaxHighlighter>
            ) : (
              <code {...rest} className={className}>
                {children}
              </code>
            )
          }
        }}
      />

      <input
        value={query}
        onChange={(e) => setQuery(e.target.value)}
      />
      <button onClick={triggerQuery}>Submit</button>
    </>
  )
}

export default App
