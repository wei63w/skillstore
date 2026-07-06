<template>
  <section class="role-page">
    <p class="role-page__eyebrow">Admin</p>
    <h1>平台运营后台</h1>
    <div class="flow-grid">
      <form class="flow-panel" @submit.prevent="handleLogin">
        <h2>管理员登录</h2>
        <input v-model="username" placeholder="用户名" />
        <input v-model="email" placeholder="邮箱" />
        <input v-model="credential" placeholder="密码" type="password" />
        <button type="submit">注册并登录</button>
      </form>

      <div class="flow-panel">
        <h2>待审核 Skill</h2>
        <button type="button" :disabled="!session.token" @click="loadPending">刷新待审</button>
        <ul>
          <li v-for="skill in pending" :key="skill.id">
            <strong>{{ skill.name }}</strong>
            <span>{{ skill.summary }}</span>
            <button type="button" @click="approve(skill.id)">通过</button>
            <button type="button" @click="reject(skill.id)">拒绝</button>
          </li>
        </ul>
      </div>
    </div>
    <p class="flow-message">{{ message }}</p>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { fetchPendingReviews, loginUser, registerUser, reviewSkill } from '../../api/storeApi';
import type { SkillListing } from '../../api/storeApi';
import { useSessionStore } from '../../stores/sessionStore';

const session = useSessionStore();
const username = ref('admin-demo');
const email = ref('admin-demo@example.com');
const credential = ref('');
const pending = ref<SkillListing[]>([]);
const message = ref('');

async function handleLogin() {
  try {
    await registerUser({ username: username.value, email: email.value, password: credential.value, role: 'ADMIN' });
  } catch {
    // Existing demo account is acceptable.
  }
  const login = await loginUser({ username: username.value, password: credential.value });
  session.setSession(login);
  message.value = `已登录：${login.user.username}`;
}

async function loadPending() {
  pending.value = await fetchPendingReviews(session.token);
}

async function approve(skillId: string) {
  await reviewSkill(session.token, skillId, 'APPROVE', '演示审核通过');
  message.value = '审核已通过';
  await loadPending();
}

async function reject(skillId: string) {
  await reviewSkill(session.token, skillId, 'REJECT', '演示审核拒绝');
  message.value = '审核已拒绝';
  await loadPending();
}
</script>
