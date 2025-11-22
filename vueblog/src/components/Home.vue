<template>
  <el-container class="home_container">
    <el-header>
      <div class="home_title">博客管理平台</div>
      <div class="home_userinfoContainer">
        <el-dropdown @command="handleCommand">
  <span class="el-dropdown-link home_userinfo">
    {{currentUserName}}<i class="el-icon-arrow-down el-icon--right home_userinfo"></i>
  </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="sysMsg">系统消息</el-dropdown-item>
            <el-dropdown-item command="MyArticle">我的文章</el-dropdown-item>
            <el-dropdown-item command="MyHome">个人主页</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <el-menu
          default-active="0"
          class="el-menu-vertical-demo" style="background-color: #ECECEC" router>
          <template v-for="(item,index) in this.$router.options.routes" v-if="!item.hidden">
            <el-submenu :index="index+''" v-if="item.children && item.children.length>1" :key="index">
              <template slot="title">
                <i :class="item.iconCls"></i>
                <span>{{item.name}}</span>
              </template>
              <el-menu-item v-for="child in item.children" v-if="!child.hidden" :index="child.path" :key="child.path">
                {{child.name}}
              </el-menu-item>
            </el-submenu>
            <template v-else-if="item.children && item.children.length==1 && !item.children[0].hidden">
              <el-menu-item :index="item.children[0].path">
                <i :class="item.children[0].iconCls"></i>
                <span slot="title">{{item.children[0].name}}</span>
              </el-menu-item>
            </template>
          </template>
        </el-menu>
      </el-aside>
      <el-container>
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-text="this.$router.currentRoute.name"></el-breadcrumb-item>
          </el-breadcrumb>
          <!-- 当访问 /home 时展示功能入口面板，避免空白 -->
          <div v-if="$route.path === '/home'" class="home_dashboard">
            <el-row :gutter="20" type="flex" justify="start">
              <el-col :span="6">
                <el-card>
                  <el-button type="primary" icon="el-icon-document" @click="$router.push('/articleList')">文章列表</el-button>
                </el-card>
              </el-col>
              <el-col :span="6" v-if="isLoggedIn">
                <el-card>
                  <el-button type="success" icon="el-icon-edit" @click="$router.push('/postArticle')">发表文章</el-button>
                </el-card>
              </el-col>
              <el-col :span="6" v-if="isAdmin">
                <el-card>
                  <el-button type="warning" icon="el-icon-user" @click="$router.push('/user')">用户管理</el-button>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card>
                  <el-button type="info" icon="el-icon-menu" @click="$router.push('/cateMana')">栏目管理</el-button>
                </el-card>
              </el-col>
            </el-row>
            <el-row :gutter="20" style="margin-top:12px">
              <el-col :span="6">
                <el-card>
                  <el-button plain icon="el-icon-data-analysis" @click="$router.push('/charts')">数据统计</el-button>
                </el-card>
              </el-col>
              <el-col :span="6" v-if="isLoggedIn">
                <el-card>
                  <el-button plain icon="el-icon-folder" @click="$router.push('/myArticles')">我的文章</el-button>
                </el-card>
              </el-col>
              <el-col :span="6" v-if="isLoggedIn">
                <el-card>
                  <el-button plain icon="el-icon-user-solid" @click="$router.push('/myHome')">个人主页</el-button>
                </el-card>
              </el-col>
            </el-row>
          </div>
          <keep-alive>
            <router-view v-if="this.$route.meta.keepAlive"></router-view>
          </keep-alive>
          <router-view v-if="!this.$route.meta.keepAlive"></router-view>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>
<script>
  import {getRequest} from '../utils/api'
  export default{
    methods: {
      handleCommand(command){
        var _this = this;
        if (command == 'logout') {
          this.$confirm('注销登录吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(function () {
            getRequest("/logout")
            _this.currentUserName = '游客';
            _this.$router.replace({path: '/'});
          }, function () {
            //取消
          })
        } else if (command == 'MyArticle') {
          // 跳转到我的文章（只显示当前用户的文章）
          this.$router.push({path: '/myArticles'});
        } else if (command == 'MyHome') {
            // 跳转到个人主页
            this.$router.push({path: '/myHome'});
        } else if (command == 'sysMsg') {
          // 简单提示，项目中暂无系统消息页面
          this.$message({type: 'info', message: '当前暂无系统消息'});
        }
      }
    },
    mounted: function () {
      var _this = this;
      getRequest("/currentUserName").then(function (msg) {
        _this.currentUserName = msg.data;
      }, function (msg) {
        _this.currentUserName = '游客';
      });
      // 获取是否已登录及管理员状态
      getRequest('/currentUser').then(function (resp) {
        if (resp.status === 200 && resp.data && resp.data.id) {
          _this.isLoggedIn = true;
        }
      }, function () {
        _this.isLoggedIn = false;
      });
      getRequest('/isAdmin').then(function (resp) {
        if (resp.status === 200) {
          _this.isAdmin = resp.data === true || resp.data === 'true';
        }
      }, function () {
        _this.isAdmin = false;
      });
    },
    data(){
      return {
        currentUserName: '',
        isLoggedIn: false,
        isAdmin: false
      }
    }
  }
</script>
<style>
  .home_container {
    height: 100%;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100%;
  }

  .el-header {
    background-color: #20a0ff;
    color: #333;
    text-align: center;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .el-aside {
    background-color: #ECECEC;
  }

  .el-main {
    background-color: #fff;
    color: #000;
    text-align: center;
  }

  .home_title {
    color: #fff;
    font-size: 22px;
    display: inline;
  }

  .home_userinfo {
    color: #fff;
    cursor: pointer;
  }

  .home_userinfoContainer {
    display: inline;
    margin-right: 20px;
  }
</style>
