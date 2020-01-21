var path = require('path')
module.exports = {
  "transpileDependencies": [
    "vuetify"
  ],
  devServer: {
    port: 8080,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8081'
      }
    }
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@src': path.resolve(__dirname, 'src/'),
        '@component': path.resolve(__dirname, 'src/components/'),
        '@apis': path.resolve(__dirname, 'src/apis/'),
        '@plugins': path.resolve(__dirname, 'src/plugins/'),
        '@router': path.resolve(__dirname, 'src/router/')
      }
    },
  }
}