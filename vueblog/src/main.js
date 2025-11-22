import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// import './styles/element-variables.scss'
import 'font-awesome/css/font-awesome.min.css'
import './utils/filter_utils.js'
import {getRequest} from './utils/api'

Vue.use(ElementUI)
Vue.config.productionTip = false
window.bus = new Vue();
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {App}
})

// 全局路由鉴权：对标记了 requiresAuth 的路由，确保当前用户已登录，否则重定向到登录页（/）
router.beforeEach((to, from, next) => {
  if (to.meta && to.meta.requiresAuth) {
    getRequest('/currentUser').then(resp => {
      if (resp.status === 200 && resp.data && resp.data.id) {
        next();
      } else {
        next({path: '/'});
      }
    }).catch(() => {
      next({path: '/'});
    });
  } else {
    next();
  }
});

// 心跳：每 5 分钟调用一次后端 keepalive 接口以续期会话
try {
  setInterval(function () {
    // 发起不干扰用户体验的请求；若失败，不做自动跳转（api.js 已处理）
    getRequest('/session/keepalive').then(()=>{
      // no-op
    }).catch(()=>{
      // ignore
    })
  }, 5 * 60 * 1000);
} catch (e) {
  // ignore
}
