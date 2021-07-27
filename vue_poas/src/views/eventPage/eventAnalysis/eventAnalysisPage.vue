<template>
    <div>
        <div class="bottom_box" style="font-size: large">
            <p id="p" v-text="title" style="text-align:center;padding: 0.2rem;font-size: xx-large"></p>
        </div>
        <div class="content-box">
            <dv-border-box1>
                <event-keyword :eventName="eventName"></event-keyword>
            </dv-border-box1>
            <dv-border-box1>
                <event-spread-trend :eventName="eventName"></event-spread-trend>
            </dv-border-box1>
            <dv-border-box1>
                <event-spread-people :eventName="eventName"></event-spread-people>
            </dv-border-box1>
        </div>
        <div class="content-box">
            <dv-border-box6>
                <event-increase :eventName="eventName"></event-increase>
            </dv-border-box6>
        </div>


<!--        <div class="content-box">-->
<!--            <dv-border-box1>-->
<!--                <event-keyword-trend></event-keyword-trend>-->
<!--            </dv-border-box1>-->
<!--        </div>-->
    </div>
</template>

<script>
    import eventKeyword from "./eventKeyword";
    import eventSpreadPeople from "./eventSpreadPeople";
    import eventSpreadTrend from "./eventSpreadTrend";
    import eventIncrease from "./eventIncrease";

    export default {
        name: "eventAnalysisPage",
        components: {
            eventKeyword,
            eventSpreadTrend,
            eventSpreadPeople,
            eventIncrease
            // eventKeywordTrend
        },
        data () {
            return {
                eventName: this.$route.query.eventName,
                title: "#"+this.$route.query.eventName+"#"
            }
        }
    }
</script>

<style scoped>

</style>