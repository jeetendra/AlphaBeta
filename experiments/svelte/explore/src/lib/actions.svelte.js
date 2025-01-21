import Typewriter from 'typewriter-effect/dist/core';

export function typewriter(node, options) {

    $effect(() => {
        let tw = new Typewriter(node, {
            autoStart: true,
            ...options
            });
        return () => {  
            tw = null;
        }
    });
}