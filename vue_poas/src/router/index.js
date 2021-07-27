import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        name: 'loginPage',
        component: () => import('../views/loginPage.vue')
    },
    {
        path: '/adminMainPage',
        name: 'adminMainPage',
        component: () => import('../views/adminMainPage.vue'),
        meta: {
            requireAuth: true
        }
    },
    {
        path: '/index',
        name: 'index',
        component: () => import('../views/index.vue'),
        redirect: '/mainPage',
        keepAlive: true,
        meta: {
            requireAuth: true
        },
        children: [
            {
                path: '/mainPage',
                name: 'mainPage',
                component: () => import('../views/mainPage'),
                redirect: "/mainPage/opinionAllPage",
                children: [
                    {
                        path: '/mainPage/opinionAllPage',
                        name: "opinionAllPage",
                        component: () => import('../views/mainPage/opinionAllPage')
                    }
                ]
            },
            {
                path: '/mainPage/opinionSearchPage',
                name: "opinionSearchPage",
                component: () => import('../views/mainPage/opinionSearchPage')
            },
            {
                path: '/eventPage',
                name: 'eventPage',
                keepAlive: true,
                component: () => import('../views/eventPage'),
                redirect: '/eventPage/eventAnalysis',
                children: [
                    {
                        path: '/eventPage/eventAnalysis',
                        name: 'eventAnalysisPage',
                        component: () => import('../views/eventPage/eventAnalysis/eventAnalysisPage')
                    },
                    {
                        path: '/eventPage/eventArticleAnalysis',
                        name: 'eventArticleAnalysisPage',
                        component: () => import('../views/eventPage/eventArticleAnalysis/eventArticleAnalysisPage')
                    },
                    {
                        path: '/eventPage/eventArticleCommentAnalysis',
                        name: 'eventArticleCommentAnalysisPage',
                        component: () => import('../views/eventPage/eventArticleCommentAnalysis/eventArticleCommentAnalysisPage')
                    }
                ]
            },
            {
                path: '/warnPage',
                name: 'warnPage',
                component: () => import('../views/warnPage')
            }
        ]
    },
];
const router = new VueRouter({
    mode: "hash", // history
    routes
});
// 导航守卫
// 参数1 : to 目标路由对象
// 参数2 : from 来源路由对象
// 参数3 : next() 下一步
// router.beforeEach((to, from, next) => {
//     // 1. 判断是不是登录页面
//     // 是登录页面
//     if(to.path === '/loginPage') {
//         next()
//     } else {
//         // 不是登录页面
//         // 2. 判断 是否登录过
//         const token = sessionStorage.getItem('isLogin')
//         token ? next() : next('/loginPage')&&alert("请先登录")
//     }
// })


export default router