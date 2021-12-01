module.exports = {
  devServer: { // 记住，别写错了devServer
    host: '0.0.0.0', // 设置本地服务器   选填
    port: 8080, // 设置本地默认端口  选填
    open: true,
    proxy: {
      '/api': {
          target: 'http://127.0.0.1',
          // 在本地会创建一个虚拟服务端，然后发送请求的数据，并同时接收请求的数据，这样服务端和服务端进行数据的交互就不会有跨域问题
          changeOrigin: true, 
          // ws: true,
          pathRewrite: {
              '^/api': '' 
          }
      }
    }
  },
  lintOnSave: false
}