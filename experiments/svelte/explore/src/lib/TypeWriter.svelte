<script>
import {typewriter} from './actions.svelte'
</script>

<div use:typewriter= {{
    strings: ['Hello', 'World']
}}></div>

<div use:typewriter= {{
    strings: ['Hello', 'Another world']
}}></div>
