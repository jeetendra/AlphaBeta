import express from 'express';
import cors from 'cors';

const app = express();

app.use(cors());

app.get('/sse', (req, res) => {
  res.setHeader('Content-Type', 'text/event-stream');
  res.setHeader('Cache-Control', 'no-cache');
  res.setHeader('Connection', 'keep-alive');

  let count = 0;
  const interval = setInterval(() => {
    count++;
    res.write(`data: Message ${count}\n\n`);
    if(count == 10) {
      clearInterval(interval);
      res.end();
    }
  }, 1000);

  req.on('close', () => {
    clearInterval(interval);

  });
});

app.listen(3000, () => {
  console.log('Server listening on port 3000');
});