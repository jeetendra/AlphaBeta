<script>
    import {onMount, tick, untrack, onDestroy} from 'svelte'
    let dialog;
    let x = $state(0);
    let y = $state(0);
    onMount(() => {
        console.log("Mounted")
    });

    $effect.pre(async () => {
        console.log("Pre effect:", x);

        // (async() => {
        //     await tick();
        // })();
        await tick();
        
        console.log("Post effect:", x);

    });

    $effect(() => {
        // untrack(() => y++); 
        console.log({x}, untrack(() => y++))
    });

    // $effect(() => {
    //     untrack(() => y++); 
    // })

    onDestroy(() => {
        console.log("Destroy|");
    });


</script>
<div>
    x: {x}
</div>

<button onclick={() => x++} >++</button>

<button onclick={() => dialog.showModal()}>Open</button>

<dialog bind:this={dialog} >
    <p>hello World</p>
    <button onclick={() => dialog.close()} >Close</button>
</dialog>