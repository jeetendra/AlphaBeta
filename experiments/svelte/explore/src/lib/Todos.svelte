<script>
    let todos = $state([
        {id:generateId(), text: "Hello", completed: false },
        {id:generateId(), text: "World", completed: true },
    ]);

    let editingTodo = $state({id:generateId(), text: "", completed: false });

    let isEditing = $state(false);

    function generateId() {
        return window.crypto.randomUUID();
    }

    function addTodo(event) {
        if (event.key !== "Enter") return;

        todos.push({...editingTodo});

        // editingTodo.id = generateId();
        // editingTodo.text = "";
        // editingTodo.completed = false;
        editingTodo = {id:generateId(), text: "", completed: false };
        isEditing = false;
    }

    function editTodo(id) {
        isEditing = true;
        const todoToEdit = todos.find(el => el.id == id);       
        editingTodo = {...todoToEdit};
        todos = todos.filter(el => el.id != id);
    }
</script>

<div class="todos">
    
    <div class="todo" >
        <input type="text" placeholder="Enter todo text" onkeydown={addTodo} bind:value={editingTodo.text} />
        <input type="checkbox" bind:checked={editingTodo.completed} />
    </div>
    {#each todos as {id, text, completed}}
        <div class="todo">
            <input readonly type="text" data-index={id} value={text} />
            <input type="checkbox"  data-index={id} checked={completed} />
            <button disabled={isEditing} onclick={() => editTodo(id)} >Edit</button>
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
