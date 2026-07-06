import { describe, expect, it } from 'vitest';
import { routes } from '../src/router/routes';
import { useSessionStore } from '../src/stores/sessionStore';
import { createPinia, setActivePinia } from 'pinia';

describe('store core flow frontend shell', () => {
  it('keeps buyer, creator and admin entries for the closed loop', () => {
    expect(routes.map((route) => route.path)).toEqual(['/', '/creator', '/admin']);
    expect(routes.map((route) => route.name)).toEqual(['buyer-home', 'creator-home', 'admin-home']);
  });

  it('stores demo token and role for role-based actions', () => {
    setActivePinia(createPinia());
    const session = useSessionStore();

    session.setSession({
      token: 'demo-token',
      user: {
        id: 'user-1',
        username: 'buyer',
        email: 'buyer@example.com',
        role: 'BUYER'
      }
    });

    expect(session.token).toBe('demo-token');
    expect(session.user?.role).toBe('BUYER');
    expect(session.isAuthenticated).toBe(true);
  });
});
