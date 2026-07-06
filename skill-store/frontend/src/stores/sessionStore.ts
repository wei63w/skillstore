import { defineStore } from 'pinia';

export type StoreRole = 'BUYER' | 'CREATOR' | 'ADMIN';

export interface StoreUser {
  id: string;
  username: string;
  email: string;
  role: StoreRole;
}

export interface StoreSession {
  token: string;
  user: StoreUser;
}

export const useSessionStore = defineStore('session', {
  state: () => ({
    token: '',
    user: null as StoreUser | null
  }),
  getters: {
    isAuthenticated: (state) => Boolean(state.token && state.user),
    role: (state) => state.user?.role ?? 'GUEST'
  },
  actions: {
    setSession(session: StoreSession) {
      this.token = session.token;
      this.user = session.user;
    },
    clearSession() {
      this.token = '';
      this.user = null;
    }
  }
});
