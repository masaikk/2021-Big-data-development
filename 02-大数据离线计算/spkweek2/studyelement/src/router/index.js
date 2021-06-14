import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Home from '../components/Home'
import AddFile from '../components/AddFile'
import inputwatch from '../components/inputwatch'
import setConfig from "../components/setConfig"
import showTables from "../components/showTables";
import fetchTableInfo from "../components/fetchTableInfo";
import queryFields from '../components/queryFields'
import vueRsource from 'vue-resource'

Vue.use(vueRsource);
Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/home',
      component: Home
    },
    {
      path: '/',
      component: setConfig
    },
    {
      path: '/AddFile',
      name: '/AddFile',
      component: AddFile
    },
    {
      path: "/inputwatch",
      name: "/inputwatch",
      component: inputwatch
    },
    {
      path: "/set",
      name: "/set",
      component: setConfig
    },
    {
      path: "/tables",
      name: "/tables",
      component: showTables
    },
    {
      path: "/data",
      name: "/data",
      component: fetchTableInfo
    },{
      path: "/field",
      name: "/field",
      component: queryFields
    }
  ]
})

export default router
