<template>
  <div class="main">
  <keep-alive>
    <router-view v-if="keepAlive" />
  </keep-alive>
  <router-view v-if="!keepAlive" />
  </div>
</template>

<script>
  export default {
    props: {
      closeTab: {
        type: Function,
        default: null
      }
    },
    name: "RouteView",
    computed: {
      keepAlive () {
        return this.$route.meta.keepAlive
      }
    },
    methods: {
      closeRouteViewTab(key) {
        if(this.closeTab){
          this.closeTab(key);
        }
      }
    }
  }
</script>