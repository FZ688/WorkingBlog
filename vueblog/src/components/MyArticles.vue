<template>
  <el-container class="my_articles">
    <el-main class="main">
      <h2 style="text-align: left; margin-bottom: 10px">我的文章</h2>
      <blog_table state="-1" :showEdit="true" :showDelete="true" :activeName="'myArticles'"></blog_table>
    </el-main>
  </el-container>
</template>

<script>
  import BlogTable from '@/components/BlogTable'
  export default {
    components: {
      'blog_table': BlogTable
    }
  }
</script>

<style>
  .my_articles > .main {
    display: flex;
    flex-direction: column;
    padding-left: 0px;
    background-color: #fff;
    padding-top: 0px;
    margin-top: 8px;
  }
</style>

