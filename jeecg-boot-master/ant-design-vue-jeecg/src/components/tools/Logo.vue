<template>
  <div class="logo">
    <router-link :to="{name:'dashboard'}">

      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->
      <img v-if="navTheme === 'dark'" :src="getAvatarView()" alt="logo">
      <img v-else :src="getAvatarView()" alt="logo">
      <!-- update-begin- author:sunjianlei --- date:20190814 --- for: logo颜色根据主题颜色变化 -->

      <h1 v-if="showTitle">{{ titlecpu }}</h1>
    </router-link>
  </div>
</template>

<script>
  import { mixin } from '@/utils/mixin.js'

  export default {
    name: 'Logo',
    mixins: [mixin],
    props: {
      title: {
        type: String,
        default: 'WENJIANG ERP',
        required: false
      },
      showTitle: {
        type: Boolean,
        default: true,
        required: false
      }
    },
    data () {

    },
    methods: {
      getAvatarView(){
        // debugger
        return this.url.imgerver +"/"+ this.userInfo.companyAvatar;
      }
    },
    computed:{
      userInfo() {
        return this.$store.getters.userInfo
      },
      titlecpu () {
        return this.userInfo.companyEn + " ERP";
      },
      url() {
        return {imgerver: window._CONFIG['domianURL']+"/sys/common/view"}
      }
    }
  }
</script>
<style lang="scss" scoped>
  /*缩小首页布 局顶部的高度*/
  $height: 59px;

  .sider {
    box-shadow: none !important;
    .logo {
      height: $height !important;
      line-height: $height !important;
      box-shadow: none !important;
      transition: background 300ms;

      a {
        color: white;
        &:hover {
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }

    &.light .logo {
      background-color: #1890ff;
    }
  }
</style>