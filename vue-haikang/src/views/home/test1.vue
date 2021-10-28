import request from '@/utils/http'

<template>
  <div class="main">
  <h1> 视频</h1>
  <h1>${}</h1>
  </div>
</template>

<script>
export function getInfo() {
  return request({
    url: '/hello',
    method: 'get',
    
  });
}

</script>