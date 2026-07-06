import { render, screen } from '@testing-library/vue';
import { createPinia } from 'pinia';
import { describe, expect, it } from 'vitest';
import BuyerHome from '../src/pages/buyer/BuyerHome.vue';
import CreatorHome from '../src/pages/creator/CreatorHome.vue';
import AdminHome from '../src/pages/admin/AdminHome.vue';

const renderWithPinia = (component: unknown) => render(component, {
  global: {
    plugins: [createPinia()]
  }
});

describe('商城角色入口页', () => {
  it('渲染买家首页占位', () => {
    renderWithPinia(BuyerHome);
    expect(screen.getByText('买家前台')).toBeTruthy();
  });

  it('渲染创作者后台占位', () => {
    renderWithPinia(CreatorHome);
    expect(screen.getByText('创作者后台')).toBeTruthy();
  });

  it('渲染平台运营后台占位', () => {
    renderWithPinia(AdminHome);
    expect(screen.getByText('平台运营后台')).toBeTruthy();
  });
});
