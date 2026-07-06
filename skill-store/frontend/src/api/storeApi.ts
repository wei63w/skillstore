import axios from 'axios';

export const storeApi = axios.create({
  baseURL: '/api/store',
  timeout: 10000
});
