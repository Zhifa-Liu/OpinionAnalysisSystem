<template>
    <div id="eventArticleSpreadPath">
        <div id="graph"></div>
    </div>
</template>

<script>
    export default {
        name: "eventArticleSpreadPath",
        props: ['eventName'],
        mounted() {
            this.init()
        },
        methods: {
            init(){
                var chartDom = document.getElementById('graph');
                var myChart = this.$echarts.init(chartDom);
                var option;

                myChart.setOption(option = {
                    tooltip: {
                        trigger: 'item',
                        triggerOn: 'mousemove'
                    },
                    series: [
                        {
                            type: 'tree',

                            data: [
                                {
                                    "name": "flare",
                                    "children": [
                                        {
                                            "name": "analytics",
                                            "children": [
                                                {
                                                    "name": "cluster",
                                                    "children": [
                                                        {"name": "AgglomerativeCluster", "value": 3938},
                                                        {"name": "CommunityStructure", "value": 3812},
                                                        {"name": "HierarchicalCluster", "value": 6714},
                                                        {"name": "MergeEdge", "value": 743}
                                                    ]
                                                },
                                                {
                                                    "name": "graph",
                                                    "children": [
                                                        {"name": "BetweennessCentrality", "value": 3534},
                                                        {"name": "LinkDistance", "value": 5731},
                                                        {"name": "MaxFlowMinCut", "value": 7840},
                                                        {"name": "ShortestPaths", "value": 5914},
                                                        {"name": "SpanningTree", "value": 3416}
                                                    ]
                                                },
                                                {
                                                    "name": "optimization",
                                                    "children": [
                                                        {"name": "AspectRatioBanker", "value": 7074}
                                                    ]
                                                }
                                            ]
                                        },
                                        {
                                            "name": "display",
                                            "children": [
                                                {"name": "DirtySprite", "value": 8833},
                                                {"name": "LineSprite", "value": 1732},
                                                {"name": "RectSprite", "value": 3623},
                                                {"name": "TextSprite", "value": 10066}
                                            ]
                                        },
                                        {
                                            "name": "flex",
                                            "children": [
                                                {"name": "FlareVis", "value": 4116}
                                            ]
                                        },
                                        {
                                            "name": "scale",
                                            "children": [
                                                {"name": "IScaleMap", "value": 2105},
                                                {"name": "LinearScale", "value": 1316},
                                                {"name": "LogScale", "value": 3151},
                                                {"name": "OrdinalScale", "value": 3770},
                                                {"name": "QuantileScale", "value": 2435},
                                                {"name": "QuantitativeScale", "value": 4839},
                                                {"name": "RootScale", "value": 1756},
                                                {"name": "Scale", "value": 4268},
                                                {"name": "ScaleType", "value": 1821},
                                                {"name": "TimeScale", "value": 5833}
                                            ]
                                        },
                                        {
                                            "name": "vis",
                                            "children": [
                                                {
                                                    "name": "axis",
                                                    "children": [
                                                        {"name": "Axes", "value": 1302},
                                                        {"name": "Axis", "value": 24593},
                                                        {"name": "AxisGridLine", "value": 652},
                                                        {"name": "AxisLabel", "value": 636},
                                                        {"name": "CartesianAxes", "value": 6703}
                                                    ]
                                                },
                                                {
                                                    "name": "controls",
                                                    "children": [
                                                        {"name": "AnchorControl", "value": 2138},
                                                        {"name": "ClickControl", "value": 3824},
                                                        {"name": "Control", "value": 1353},
                                                        {"name": "ControlList", "value": 4665},
                                                        {"name": "DragControl", "value": 2649},
                                                        {"name": "ExpandControl", "value": 2832},
                                                        {"name": "HoverControl", "value": 4896},
                                                        {"name": "IControl", "value": 763},
                                                        {"name": "PanZoomControl", "value": 5222},
                                                        {"name": "SelectionControl", "value": 7862},
                                                        {"name": "TooltipControl", "value": 8435}
                                                    ]
                                                },
                                                {
                                                    "name": "data",
                                                    "children": [
                                                        {"name": "Data", "value": 20544},
                                                        {"name": "DataList", "value": 19788},
                                                        {"name": "DataSprite", "value": 10349},
                                                        {"name": "EdgeSprite", "value": 3301},
                                                        {"name": "NodeSprite", "value": 19382},
                                                        {
                                                            "name": "render",
                                                            "children": [
                                                                {"name": "ArrowType", "value": 698},
                                                                {"name": "EdgeRenderer", "value": 5569},
                                                                {"name": "IRenderer", "value": 353},
                                                                {"name": "ShapeRenderer", "value": 2247}
                                                            ]
                                                        },
                                                        {"name": "ScaleBinding", "value": 11275},
                                                        {"name": "Tree", "value": 7147},
                                                        {"name": "TreeBuilder", "value": 9930}
                                                    ]
                                                },
                                                {
                                                    "name": "events",
                                                    "children": [
                                                        {"name": "DataEvent", "value": 2313},
                                                        {"name": "SelectionEvent", "value": 1880},
                                                        {"name": "TooltipEvent", "value": 1701},
                                                        {"name": "VisualizationEvent", "value": 1117}
                                                    ]
                                                },
                                                {
                                                    "name": "legend",
                                                    "children": [
                                                        {"name": "Legend", "value": 20859},
                                                        {"name": "LegendItem", "value": 4614},
                                                        {"name": "LegendRange", "value": 10530}
                                                    ]
                                                },
                                                {
                                                    "name": "operator",
                                                    "children": [
                                                        {
                                                            "name": "distortion",
                                                            "children": [
                                                                {"name": "BifocalDistortion", "value": 4461},
                                                                {"name": "Distortion", "value": 6314},
                                                                {"name": "FisheyeDistortion", "value": 3444}
                                                            ]
                                                        },
                                                        {
                                                            "name": "encoder",
                                                            "children": [
                                                                {"name": "ColorEncoder", "value": 3179},
                                                                {"name": "Encoder", "value": 4060},
                                                                {"name": "PropertyEncoder", "value": 4138},
                                                                {"name": "ShapeEncoder", "value": 1690},
                                                                {"name": "SizeEncoder", "value": 1830}
                                                            ]
                                                        },
                                                        {
                                                            "name": "filter",
                                                            "children": [
                                                                {"name": "FisheyeTreeFilter", "value": 5219},
                                                                {"name": "GraphDistanceFilter", "value": 3165},
                                                                {"name": "VisibilityFilter", "value": 3509}
                                                            ]
                                                        },
                                                        {"name": "IOperator", "value": 1286},
                                                        {
                                                            "name": "label",
                                                            "children": [
                                                                {"name": "Labeler", "value": 9956},
                                                                {"name": "RadialLabeler", "value": 3899},
                                                                {"name": "StackedAreaLabeler", "value": 3202}
                                                            ]
                                                        },
                                                        {
                                                            "name": "layout",
                                                            "children": [
                                                                {"name": "AxisLayout", "value": 6725},
                                                                {"name": "BundledEdgeRouter", "value": 3727},
                                                                {"name": "CircleLayout", "value": 9317},
                                                                {"name": "CirclePackingLayout", "value": 12003},
                                                                {"name": "DendrogramLayout", "value": 4853},
                                                                {"name": "ForceDirectedLayout", "value": 8411},
                                                                {"name": "IcicleTreeLayout", "value": 4864},
                                                                {"name": "IndentedTreeLayout", "value": 3174},
                                                                {"name": "Layout", "value": 7881},
                                                                {"name": "NodeLinkTreeLayout", "value": 12870},
                                                                {"name": "PieLayout", "value": 2728},
                                                                {"name": "RadialTreeLayout", "value": 12348},
                                                                {"name": "RandomLayout", "value": 870},
                                                                {"name": "StackedAreaLayout", "value": 9121},
                                                                {"name": "TreeMapLayout", "value": 9191}
                                                            ]
                                                        },
                                                        {"name": "Operator", "value": 2490},
                                                        {"name": "OperatorList", "value": 5248},
                                                        {"name": "OperatorSequence", "value": 4190},
                                                        {"name": "OperatorSwitch", "value": 2581},
                                                        {"name": "SortOperator", "value": 2023}
                                                    ]
                                                },
                                                {"name": "Visualization", "value": 16540}
                                            ]
                                        }
                                    ]
                                }
                            ],

                            top: '1%',
                            left: '7%',
                            bottom: '1%',
                            right: '20%',

                            symbolSize: 7,

                            label: {
                                position: 'left',
                                verticalAlign: 'middle',
                                align: 'right',
                                fontSize: 9
                            },

                            leaves: {
                                label: {
                                    position: 'right',
                                    verticalAlign: 'middle',
                                    align: 'left'
                                }
                            },

                            emphasis: {
                                focus: 'descendant'
                            },

                            expandAndCollapse: true,
                            animationDuration: 550,
                            animationDurationUpdate: 750
                        }
                    ]
                });

                option && myChart.setOption(option);
            }
        }
    }
</script>

<style scoped>
    #eventArticleSpreadPath {
        /*background-color: white;*/
        padding: 0.2rem 0.2rem 0;
        height: 5rem;
        width: 23.5rem;
    }

    #graph {
        height: 4.9rem;
        width: 23.5rem;
    }
</style>