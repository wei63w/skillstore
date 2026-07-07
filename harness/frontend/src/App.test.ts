import { describe, expect, it, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import { createPinia } from 'pinia';
import App from './App.vue';

vi.mock('./api/harnessApi', () => ({
  harnessApi: {
    listProviders: vi.fn().mockResolvedValue([{ providerKey: 'stub', configured: true, message: '可用' }]),
    listRuns: vi.fn().mockResolvedValue([]),
    getRun: vi.fn(),
    getLogs: vi.fn().mockResolvedValue(''),
    startPipeline: vi.fn()
  }
}));

describe('Harness Console', () => {
  it('renders three working regions', () => {
    const wrapper = mount(App, {
      global: {
        plugins: [createPinia()]
      }
    });

    expect(wrapper.text()).toContain('Harness Console');
    expect(wrapper.text()).toContain('Pipeline');
    expect(wrapper.text()).toContain('日志');
    expect(wrapper.text()).toContain('审批');
    expect(wrapper.text()).toContain('Diff');
  });
});
