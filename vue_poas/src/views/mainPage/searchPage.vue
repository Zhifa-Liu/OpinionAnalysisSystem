<template>
    <div id="search" style="height: 10rem">
        <i class="el-icon-data-board" style="margin-left: 40%;margin-top: 1rem;font-size: xx-large"> 热点舆情事件搜索 </i>
        <div style="text-align:center;margin-top: 1rem;color: #4b67af">
            <el-autocomplete
                    class="inline-input"
                    v-model="state1"
                    :fetch-suggestions="querySearch"
                    placeholder="请输入内容"
                    @select="handleSelect"
                    :trigger-on-focus="false"
                    style="width:15rem;margin-bottom: 0.5rem"
            >
                <el-button slot="append" icon="el-icon-search">搜索</el-button>
            </el-autocomplete>
            <div>
                <i class="el-icon-search" style="font-size: large;margin-right: 0.3rem;color: white">搜索热词:</i>
                <el-tag style="margin-right: 0.1rem">疫情</el-tag>
                <el-tag type="success" style="margin-right: 0.1rem">袁隆平</el-tag>
                <el-tag type="info" style="margin-right: 0.1rem">甘肃马拉松</el-tag>
                <el-tag type="warning" style="margin-right: 0.1rem">地震</el-tag>
                <el-tag type="danger">躺平</el-tag>
            </div>

            <el-carousel :interval="4000" type="card" height="300px" :style="{marginRight:'2rem',marginLeft:'2rem',marginTop:'0.5rem'}">
                <!--                <el-carousel-item v-for="item in 6" :key="item">-->
                <!--                    <h3 class="medium">{{ item }}</h3>-->
                <!--                </el-carousel-item>-->
                <el-carousel-item v-for="item in imgList" :key="item.id">
                    <img style="width: 100%; height: auto;" :src="item.idView" class="image" :title="item.title" alt="">
                </el-carousel-item>
            </el-carousel>
        </div>

    </div>
</template>

<script>
    export default {
        name: "searchPage",

        data() {
            return {
                imgList: [
                    {id:0,idView:require('../../assets/1.jpg'),title:'普京发表新政策'},
                    {id:1,idView:require('../../assets/2.jpg'),title:'普京发表新政策'},
                    {id:2,idView:require('../../assets/3.jpg'),title:'普京发表新政策'},
                    {id:3,idView:require('../../assets/4.jpg'),title:'普京发表新政策'},
                    {id:4,idView:require('../../assets/5.jpg'),title:'普京发表新政策'},
                    {id:5,idView:require('../../assets/6.jpg'),title:'普京发表新政策'}
                ],
                restaurants: [],
                state1: '',
                state2: ''
            };
        },
        methods: {
            querySearch(queryString, cb) {
                var restaurants = this.restaurants;
                var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
                // 调用 callback 返回建议列表的数据
                cb(results);
            },
            createFilter(queryString) {
                return (restaurant) => {
                    return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1);
                };
            },
            loadAll() {
                return [
                    { "value": "陈薇称正在申请吸入式新冠疫苗紧急使用", "address": "长宁区新渔路144号" },
                    { "value": "兰新铁路甘肃段发生事故致9人遇难 ", "address": "上海市长宁区淞虹路661号" },
                    { "value": "吸入式新冠疫苗可实现黏膜免疫 ", "address": "上海市普陀区真北路988号创邑金沙谷6号楼113" },
                    { "value": "上海合作组织民间友好论坛在武汉开幕", "address": "天山西路438号" },
                    { "value": "全国就业创业工作暨普通高等学校毕业生就业创业", "address": "上海市长宁区金钟路968号1幢18号楼一层商铺18-101" },
                    { "value": "国办发布关于推动公立医院高质量发展的意见", "address": "上海市长宁区金钟路633号" },
                    { "value": "外交部回应美国将59家中企列入投资黑名单", "address": "上海市嘉定区曹安公路曹安路1685号" },
                    { "value": "佛山公布新增1例本土确诊详情", "address": "上海市普陀区同普路1435号" },
                    { "value": "印花税法草案二次审议稿拟适当降低税率", "address": "上海市北翟路1444弄81号B幢-107" },
                    { "value": "广州海珠区社会餐饮服务单位暂停堂食 ", "address": "上海市嘉定区新郁路817号" },
                    { "value": "腾讯副总炮轰短视频个性分发低俗化", "address": "嘉定区曹安路1611号" },
                    { "value": "2020年度人民法院环境资源典型案例 ", "address": "嘉定区曹安公路2383弄55号" },
                    { "value": "广东6月3日新增本土确诊病例7例", "address": "嘉定区江桥镇曹安公路2409号1F，2383弄62号1F" },
                    { "value": "广州本土病例病毒载量高、转阴时间长", "address": "上海长宁区金钟路968号9号楼地下一层" },
                    { "value": "广州荔湾两地调整为封闭管理区域", "address": "上海市长宁区天山西路119号" },
                    { "value": "中国邮政成立奶茶店 ", "address": "上海市长宁区仙霞西路" },
                ];
            },
            handleSelect(item) {
                console.log(item);
            }
        },
        mounted() {
            this.restaurants = this.loadAll();
        }
    }
</script>

<style scoped>
</style>