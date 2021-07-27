<template>
    <div>
        <event-search></event-search>
    </div>
</template>

<script>
    import eventSearch from "./opinionSearchPageComponents/eventSearch";

    export default {
        name: "opinionSearchPage",
        components: {
            eventSearch,
        }
    }
</script>

<style scoped>
    #eventSearch {

    }
</style>