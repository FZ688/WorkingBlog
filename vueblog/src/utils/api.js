import axios from 'axios'

let base = '';
// allow sending cookies for cross-origin requests (when backend sets Access-Control-Allow-Credentials)
axios.defaults.withCredentials = true;

// 全局响应拦截：未认证（401）时重定向到登录页，避免页面静默空白
axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
    try {
    if (error && error.response && error.response.status === 401) {
      // 如果是心跳接口不要自动跳转，心跳失败由调用方处理
      if (error.config && error.config.url && error.config.url.indexOf('/session/keepalive') !== -1) {
        return Promise.reject(error);
      }
      // 若当前已经在根路径则不重复跳转
      if (window && window.location && window.location.hash.indexOf('#/') === -1) {
        window.location.href = '/#/';
      }
    }
  } catch (e) {
    // ignore
  }
  return Promise.reject(error);
});
export const postRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      // Do whatever you want to transform the data
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
}
export const uploadFileRequest = (url, params) => {
  return axios({
    method: 'post',
    url: `${base}${url}`,
    data: params,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}
export const putRequest = (url, params) => {
  return axios({
    method: 'put',
    url: `${base}${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
}
export const deleteRequest = (url) => {
  return axios({
    method: 'delete',
    url: `${base}${url}`
  });
}
export const getRequest = (url,params) => {
  return axios({
    method: 'get',
    data:params,
    transformRequest: [function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    url: `${base}${url}`
  });
}
