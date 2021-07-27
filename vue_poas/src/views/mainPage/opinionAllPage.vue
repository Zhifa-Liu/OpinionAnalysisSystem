<template>
    <div>
        <div class="content-box">
            <dv-border-box1>
                <hot-field-page></hot-field-page>
            </dv-border-box1>
        </div>
        <div class="content-box">
            <dv-border-box-1>
                <field-heat-page></field-heat-page>
            </dv-border-box-1>
            <dv-border-box-1>
                <heat-map-page></heat-map-page>
            </dv-border-box-1>
        </div>
    </div>
</template>

<script>
    import hotFieldPage from "./opinionAllPageComponents/hotFieldPage";
    import heatMapPage from "./opinionAllPageComponents/heatMapPage";
    import fieldHeatPage from "./opinionAllPageComponents/fieldHeatPage";

    export default {
        name: "opinionAllPage",
        components: {
            hotFieldPage,
            fieldHeatPage,
            heatMapPage,
        }
    }
</script>

<style scoped>

</style>