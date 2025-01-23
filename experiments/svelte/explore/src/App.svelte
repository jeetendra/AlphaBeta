<script>
  import Counter from './lib/Counter.svelte'
  import TypeWriter from './lib/TypeWriter.svelte'
  import Swiper from './lib/Swiper.svelte';
  import Todos from './lib/Todos.svelte';
    import Lifecycle from './lib/Lifecycle.svelte';
    import Template from './lib/Template.svelte';
</script>

<main>
  <div class="card">
    <Counter />
  </div>
  <Template />
  <div class="card">
    <Lifecycle />
  </div>
  <div class="card">
    <Todos />
  </div>
  <div class="card">
    <TypeWriter />
  </div>
  <div class="card">
    <Swiper />
  </div>
</main>

<style>
</style>
