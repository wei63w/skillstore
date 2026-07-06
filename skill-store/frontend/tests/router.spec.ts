import { describe, expect, it } from 'vitest';
import { routes } from '../src/router/routes';

describe('商城前端路由骨架', () => {
  it('包含买家、创作者和平台运营三个入口', () => {
    expect(routes.map((route) => route.path)).toEqual([
      '/',
      '/creator',
      '/admin'
    ]);
  });
});
