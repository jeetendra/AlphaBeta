import { defineConfig } from 'vite';
import { nodePolyfills } from 'vite-plugin-node-polyfills'

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
    // define: {
    //     global: {}
    // }
})