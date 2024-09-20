import axios from "axios";
const instance = axios.create({
  baseURL: "http://localhost:8101/api",
  timeout: 1000000,
  headers: {},
});

instance.interceptors.response.use(
  function (response) {
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response dat
    if (response.data.code === 0) {
      return response.data.data;
    } else {
      return response.data.message;
    }
  },
  function (error) {
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    // Do something with response error
    return Promise.reject(error);
  }
);
export default instance;
