export async function ex1(element) {
  // const arr = [1,2,3,4,5,6,7,8,9,0];
  // const reader = iteratorToStream(arr).getReader();

  apicall();
  
}

function apicall() {
  const headers = new Headers();
  // headers.append('Content-Type', 'application/json');
  // headers.append('Authorization', 'Bearer your_token');
  headers.append('Accept', 'text/event-stream');

  fetch('http://localhost:3000/sse', {
    headers: headers
  }).then(response => {
    const reader = response.body.getReader();

    const read = async () => {
      const { done, value } = await reader.read(); 

      if (done) {
        console.log('Stream ended');
        return;
      }

      const decoder = new TextDecoder();
      const text = decoder.decode(value); 

      console.log(text);

      read(); 
    };

    read(); 
  })
  .catch(error => {
    console.error('Error fetching SSE stream:', error);
  });
}



export function iteratorToStream(iterator) {
  return new ReadableStream({
    async pull(controller) {
      const { value, done } = await iterator.next();

      if (value) {
        controller.enqueue(value);
      }
      if (done) {
        controller.close();
      }
    },
  });
}
