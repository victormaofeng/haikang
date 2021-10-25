<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: "app",
  components: {},
};
</script>

<style scope>
html,
body,
#app {
  height: 100%;
  width: 100%;
}
* {
  padding: 0;
  margin: 0;
}
</style>
