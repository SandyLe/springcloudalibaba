<template>
  <div>{{id}}</div>
</template>

<script>
    export default {
      name: "CustomerEdit",
      data() {
        return {
          id:0
        }
      },
      watch: {
        $route: function () {
          debugger
          this.id=this.$route.query.id
          console.log(this.$route.query.id)
        }
      }
    }
</script>

<style scoped>

</style>