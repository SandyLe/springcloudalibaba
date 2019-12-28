import Vue from 'vue'
import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import notification from 'ant-design-vue/es/notification'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { generateIndexRouter } from "@/utils/util"

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/user/login', '/user/register', '/user/register-result','/user/alteration'] // no redirect whitelist

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar

  if (Vue.ls.get(ACCESS_TOKEN)) {
    /* has token */
    if (to.path === '/user/login') {
      console.log('登录后再次进入登录页，主动引导至 个人页/工作台:/dashboard/workplace');
      next({ path: '/dashboard/workplace' })
      NProgress.done()
    } else {
      console.log('进入面板内页面:' + to.path);
      if (store.getters.permissionList.length === 0) {
        store.dispatch('GetPermissionList').then(res => {
              console.log('没有权限，重新拉取菜单权限')
              const menuData = res.result.menu;
              console.log(res.message)
              if (menuData === null || menuData === "" || menuData === undefined) {
                return;
              }
              // 此处重新将 / 更改为重定向至 /dashboard/analysis， 坑爹的写法
              let constRoutes = [];
              constRoutes = generateIndexRouter(menuData);
              console.log('重新置换路由为：');
              console.log(constRoutes);
              // 添加主界面路由
              store.dispatch('UpdateAppRouter',  { constRoutes }).then(() => {
                // 根据roles权限生成可访问的路由表
                // 动态添加可访问路由表
                router.addRoutes(store.getters.addRouters)
                const redirect = decodeURIComponent(from.query.redirect || to.path)

                if (to.path === redirect) {
                  console.log('替换redirect:'+redirect);
                  // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
                  next({ ...to, replace: true })
                } else {
                  console.log('跳转到redirect:'+redirect);
                  // 跳转到目的路由
                  next({ path: redirect })
                }
              })
            })
          .catch(() => {
           /* notification.error({
              message: '系统提示',
              description: '请求用户信息失败，请重试！'
            })*/
            store.dispatch('Logout').then(() => {
              next({ path: '/user/login', query: { redirect: to.fullPath } })
            })
          })
      } else {
        next()
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      console.log('白名单路径:' + to.path);
      next()
    } else {
      console.log('根路径:' + to.path);
      next({ path: '/user/login', query: { redirect: to.fullPath } })
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
