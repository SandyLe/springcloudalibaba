<template>
  <div id="userLayout" :class="['user-layout-wrapper', device]">
      <div class="container">
        <div class="tbar">
          <div class="tbarhead tbarinfo">欢迎使用 呵梦企业ERP 云平台！</div>
          <div class="tbarhead tbarTitle"><a href="/"><p>呵梦企业ERP · 云平台</p></a><span><img width="70px" alt="服务支持" src="~@/assets/WechatQRcode.jpeg"/></span></div>
        </div>
        <div class = "left">
          <img src="~@/assets/index.png" width="95%" style="padding-left: 15%" class="logo" alt="logo">
        </div>
        <div class = "right">
          <div class="top">
            <div class="header">
              <a href="/">
                <img src="~@/assets/userlogin.png" class="logo" alt="logo">
                <span class="title" icon="user" >用户登陆</span>
              </a>
            </div><!--
          <div class="desc">
            上海文疆商贸有限公司 · 企业ERP平台
          </div>-->
          </div>

          <route-view></route-view>

        </div>
        <div class="footer">
          <div class="links">
            <a href="http://www.wj-trading.cn/" target="_blank">上海文疆商贸 官网</a>
          </div>
          <div class="copyright">
            Copyright &copy; 2020 <a href="http://www.wj-trading.cn/" target="_blank">呵梦科技 & 上海文疆商贸</a> 出品
          </div>
        </div>
      </div>
  </div>
</template>

<script>
  import RouteView from "@/components/layouts/RouteView"
  import { mixinDevice } from '@/utils/mixin.js'

  export default {
    name: "UserLayout",
    components: { RouteView },
    mixins: [mixinDevice],
    data () {
      return {}
    },
    mounted () {
      document.body.classList.add('userLayout')
    },
    beforeDestroy () {
      document.body.classList.remove('userLayout')
    },
  }
</script>

<style lang="scss" scoped>
  body {
    margin: 0;
    padding: 0;
  }
  #userLayout.user-layout-wrapper {
    height: 100%;

    &.mobile {
      .container {
        .main {
          max-width: 368px;
          width: 98%;
        }
      }
    }

    .container {
      width: 100%;
      min-height: 100%;
      background: #f0f2f5 url(~@/assets/background.svg) no-repeat 50%;
      background-size: 100%;
      padding: 0px 0 144px;
      position: relative;

      a {
        text-decoration: none;
      }

      .top {
        text-align: center;

        .header {
          margin: 0;
          padding: 0;
          height: 44px;
          line-height: 44px;

          .badge {
            position: absolute;
            display: inline-block;
            line-height: 1;
            vertical-align: middle;
            margin-left: -12px;
            margin-top: -10px;
            opacity: 0.8;
          }

          .logo {
            height: 44px;
            vertical-align: top;
            margin-right: 16px;
            border-style: none;
          }

          .title {
            font-size: 22px;
            color: rgba(0, 0, 0, .85);
            font-family: "Chinese Quote", -apple-system, BlinkMacSystemFont, "Segoe UI", "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "Helvetica Neue", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
            font-weight: 600;
            position: relative;
            top: 2px;
          }
        }
        .desc {
          font-size: 14px;
          color: rgba(0, 0, 0, 0.45);
          margin-top: 12px;
          margin-bottom: 40px;
        }
      }

      .main {
        min-width: 260px;
        width: 368px;
        margin: 0 auto;
      }

      .footer {
        position: absolute;
        width: 100%;
        bottom: 0;
        padding: 0 16px;
        margin: 48px 0 24px;
        text-align: center;

        .links {
          margin-bottom: 8px;
          font-size: 14px;
          a {
            color: rgba(0, 0, 0, 0.45);
            transition: all 0.3s;
            &:not(:last-child) {
              margin-right: 40px;
            }
          }
        }
        .copyright {
          color: rgba(0, 0, 0, 0.45);
          font-size: 14px;
        }
      }
    }
    .left {
      width: 60%;
      float: left;
    }
    .right {
      width: 40%;
      float: left;
    }
    .tbar {
      height: 100px;
      margin-bottom: 50px;
      border-bottom: #888888 solid 1px;
      position:relative;
      .tbarhead{
        padding-left: 10%;
        margin: auto 0;
      }
      .tbarinfo{
        height: 20px;
        background-color: #333333;
        color: #f2f2f2;
        font-family: "Bangla Sangam MN";
      }
      .tbarTitle {
        padding-bottom: 0px;
        height: 80px;
        vertical-align: bottom;
      }
    }
    .tbar p{
      position:absolute;
      bottom:0px;
      padding:0px;
      margin:0px;
      font-size: 30px;
    }
    .tbar img{
      float: right;
    }
  }
</style>