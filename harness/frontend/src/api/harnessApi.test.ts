import { describe, expect, it, vi } from 'vitest';

const create = vi.fn(() => ({
  get: vi.fn(),
  post: vi.fn()
}));

vi.mock('axios', () => ({
  default: {
    create
  }
}));

describe('harnessApi', () => {
  it('uses backend port as default API base URL', async () => {
    await import('./harnessApi');

    expect(create).toHaveBeenCalledWith(expect.objectContaining({
      baseURL: 'http://localhost:8080'
    }));
  });
});
