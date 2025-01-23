{#snippet name()}
    <h1>Boom</h1>
{/snippet}

<div>
    here we will add {@render name()} to render {@render name()}
</div>

{#if 1 > 0}
    hello
{/if}

{#each [1,2,3] as data, index}
   <div>{index}: {data}</div>
{/each}