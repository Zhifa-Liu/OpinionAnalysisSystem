<template>
    <div style="height:11rem;overflow-y:auto;overflow-x:hidden">
        <div class="bottom_box" style="font-size: xx-large">
            <p id="p" v-text="title" style="text-align:center;padding: 0.2rem;font-size: xx-large"></p>
        </div>
        <div class="content-box">
            <dv-border-box1>
                <event-comment-keyword :eventName="eventName"></event-comment-keyword>
            </dv-border-box1>
            <dv-border-box1>
                <event-comment-emotion :eventName="eventName"></event-comment-emotion>
            </dv-border-box1>
            <dv-border-box1>
                <event-comment-gender :eventName="eventName"></event-comment-gender>
            </dv-border-box1>
        </div>
        <div class="content-box">
            <dv-border-box6>
                <event-comment-like-rank :eventName="eventName"></event-comment-like-rank>
            </dv-border-box6>
            <dv-border-box6>
                <event-comment-reply-rank :eventName="eventName"></event-comment-reply-rank>
            </dv-border-box6>
        </div>
    </div>
</template>

<!--<template>-->
<!--    <div>-->
<!--        <div class="content-box">-->
<!--            <dv-border-box1>-->
<!--                <event-comment-keyword></event-comment-keyword>-->
<!--            </dv-border-box1>-->
<!--            <dv-border-box1>-->
<!--                <event-comment-emotion></event-comment-emotion>-->
<!--            </dv-border-box1>-->
<!--            &lt;!&ndash;            <dv-border-box1>&ndash;&gt;-->
<!--            &lt;!&ndash;                <event-comment-region></event-comment-region>&ndash;&gt;-->
<!--            &lt;!&ndash;            </dv-border-box1>&ndash;&gt;-->
<!--        </div>-->
<!--        <div class="content-box">-->
<!--            <dv-border-box6>-->
<!--                <event-comment-data></event-comment-data>-->
<!--            </dv-border-box6>-->
<!--            <dv-border-box6>-->
<!--                <div id="reviewer_div">-->
<!--                    &lt;!&ndash;                    <dv-border-box6>&ndash;&gt;-->
<!--                    &lt;!&ndash;                        <event-comment-age></event-comment-age>&ndash;&gt;-->
<!--                    &lt;!&ndash;                    </dv-border-box6>&ndash;&gt;-->
<!--                    <dv-border-box6>-->
<!--                        <event-comment-gender></event-comment-gender>-->
<!--                    </dv-border-box6>-->
<!--                </div>-->
<!--            </dv-border-box6>-->
<!--            <dv-border-box6>-->
<!--                <event-comment-rank></event-comment-rank>-->
<!--            </dv-border-box6>-->
<!--        </div>-->
<!--    </div>-->
<!--</template>-->

<script>
    import eventCommentEmotion from "./eventCommentEmotion";
    import eventCommentKeyword from "./eventCommentKeyword";
    // import eventCommentRegion from "./eventCommentRegion";
    import eventCommentGender from "./eventCommentGender";
    // import eventCommentData from "./eventCommentStatData";
    // import eventCommentAge from "./eventCommentAge";
    // import eventCommentRank from "./eventCommentRank";
    import eventCommentLikeRank from "./eventCommentLikeRank";
    import eventCommentReplyRank from "./eventCommentReplyRank";

    export default {
        name: "eventArticleCommentAnalysisPage",
        components: {
            eventCommentKeyword,
            eventCommentEmotion,
            // eventCommentRegion,
            eventCommentGender,
            // eventCommentData,
            // eventCommentAge,
            // eventCommentRank,
            eventCommentLikeRank,
            eventCommentReplyRank,
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
    ::-webkit-scrollbar{width:0.1rem;}
    ::-webkit-scrollbar-track{background-color: #909393;}
    ::-webkit-scrollbar-thumb{background-color: #161617;}
    ::-webkit-scrollbar-thumb:hover {background-color:#9c3}
    ::-webkit-scrollbar-thumb:active {background-color: #0f0f10
    }
    /*#reviewer_div {*/
    /*    display: flex;*/
    /*    height: 4.8rem;*/
    /*    width: 11.8rem;*/
    /*}*/
</style>