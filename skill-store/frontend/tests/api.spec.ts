import { describe, expect, it } from 'vitest';
import { storeApi } from '../src/api/storeApi';

describe('商城 API 请求封装', () => {
  it('使用统一的后端前缀', () => {
    expect(storeApi.defaults.baseURL).toBe('/api/store');
  });
});
