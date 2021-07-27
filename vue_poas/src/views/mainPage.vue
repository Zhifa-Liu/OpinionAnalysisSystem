<template>
    <div>
        <div id="opinion_div">
            <div class="react-left ml-4" style="background-color: #0f1325;">
                <router-link to="/mainPage/opinionAllPage" active-class="active">
                    <span class="text page_name">舆情总缆</span>
                </router-link>
            </div>
        </div>
        <div class="body_box">
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    export default {
        name: "mainPage",
        components: {

        },
        mounted() {

        },
        methods: {

        }
    }
</script>

<style scoped>
    #opinion_div {
        display: flex;
        align-items: center;
        margin-bottom: 0.2rem;
    }
</style>