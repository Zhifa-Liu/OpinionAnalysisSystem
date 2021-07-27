<template>
  <div id="app">
    <router-view />
  </div>
</template>
<!--<script>-->

<!--  export default {-->
<!--    mounted(){-->
<!--      //此方法刷新页面时也会执行-->
<!--      window.addEventListener('onbeforeunload',()=>{-->
<!--        // window.sessionStorage["tempFlag"] = true;-->
<!--        localStorage.setItem("isLogin",true);-->
<!--      })-->
<!--    }-->
<!--  }-->
<!--</script>-->

<style lang="scss">
body {
  margin: 0;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>

