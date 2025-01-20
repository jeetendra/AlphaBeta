import { defineConfig } from 'vite';
import { nodePolyfills } from 'vite-plugin-node-polyfills';
import fs from 'fs';

export default defineConfig({
    plugins: [
        nodePolyfills(
            {
                globals: {
                    // Buffer: true, // can also be 'build', 'dev', or false
                    global: true,
                    // process: true,
                },
            }
        ),
    ],
    server: {
        https: {
            key: fs.readFileSync('../cert/key.pem'),
            cert: fs.readFileSync('../cert/cert.pem')
        }
    },

})