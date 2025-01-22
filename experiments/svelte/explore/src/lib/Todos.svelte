<script>
    let todos = $state([
        { text: "Hello", completed: false },
        { text: "World", completed: true },
    ]);

    function addTodo(event) {
        if (event.key !== "Enter") return;

        const value = event.target.value;
        event.target.value = "";
        todos.push({ text: value, completed: false });
    }
</script>

<div class="todos">
    <input type="text" placeholder="Enter todo text" onkeydown={addTodo} />

    {#each todos as todo}
        <div class="todo">
            <input type="text" value={todo.text} />
            <input type="checkbox" checked={todo.completed} />
        </div>
    {/each}
</div>

<style>
    .todos {
        display: grid;
        gap: 1rem;
        margin-block-start: 1rem;
        width: 400px;
    }

    .todo {
        position: relative;
    }

    input[type="text"] {
        width: 100%;
        padding: 1rem;
    }

    input[type="checkbox"] {
        position: absolute;
        right: -0.5rem;
        top: 50%;
        translate: 0% -50%;
    }
</style>
