<template>
    <div>
        <div class="bottom_box" style="font-size: xx-large">
            <p id="p" v-text="title" style="text-align:center;padding: 0.2rem;font-size: xx-large"></p>
        </div>
        <div class="content-box">
            <dv-border-box1>
                <event-article-top-n :eventName="eventName"></event-article-top-n>
            </dv-border-box1>
            <dv-border-box1>
                <event-article-region :eventName="eventName"></event-article-region>
            </dv-border-box1>
            <dv-border-box1>
                <event-article-hot :eventName="eventName"></event-article-hot>
            </dv-border-box1>
        </div>
        <div class="content-box">
            <dv-border-box6>
                <event-article-timeline :eventName="eventName"></event-article-timeline>
            </dv-border-box6>
        </div>
<!--        <div class="content-box">-->
<!--            <dv-border-box1>-->
<!--                <event-article-spread-path></event-article-spread-path>-->
<!--            </dv-border-box1>-->
<!--        </div>-->
    </div>
</template>

<script>
    import eventArticleTopN from "./eventArticleTopN";
    import eventArticleRegion from "./eventArticleRegion";
    import eventArticleHot from "./eventArticleHot";
    import eventArticleTimeline from "./eventArticleTimeline";

    export default {
        name: "eventArticleAnalysisPage",
        components: {
            eventArticleTopN,
            eventArticleRegion,
            eventArticleHot,
            eventArticleTimeline
            //eventArticleSpreadPath
        },
        data(){
            return {
                eventName: this.$route.query.eventName,
                title: "#"+this.$route.query.eventName+"#"
            }
        }
    }
</script>

<style scoped>

</style>