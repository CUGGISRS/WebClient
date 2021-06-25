module.exports = {
  devServer: {
    // host: 'localhost',
    // port: 8080,
    proxy: {
      '/api': {
        target: 'http://192.168.8.112:8766',
        changeOrigin: true,
        ws: true,
        secure: false,
        pathRewrite: {
          '^/api': ''
        }
      }
    }
  },
  publicPath: './'
};
