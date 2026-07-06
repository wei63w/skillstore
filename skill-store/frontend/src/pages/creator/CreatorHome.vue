<template>
  <section class="role-page">
    <p class="role-page__eyebrow">Creator</p>
    <h1>创作者后台</h1>
    <div class="flow-grid">
      <form class="flow-panel" @submit.prevent="handleLogin">
        <h2>创作者登录</h2>
        <input v-model="username" placeholder="用户名" />
        <input v-model="email" placeholder="邮箱" />
        <input v-model="credential" placeholder="密码" type="password" />
        <button type="submit">注册并登录</button>
      </form>

      <form class="flow-panel" @submit.prevent="submitSkill">
        <h2>上传 Skill</h2>
        <input v-model="skillName" placeholder="Skill 名称" />
        <input v-model="summary" placeholder="摘要" />
        <input v-model="category" placeholder="分类" />
        <input v-model.number="priceCents" min="0" step="100" type="number" />
        <input accept=".zip,.json,.txt" type="file" @change="selectFile" />
        <button type="submit" :disabled="!session.token || !file">提交审核</button>
      </form>
    </div>
    <p class="flow-message">{{ message }}</p>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { loginUser, registerUser, uploadSkill } from '../../api/storeApi';
import { useSessionStore } from '../../stores/sessionStore';

const session = useSessionStore();
const username = ref('creator-demo');
const email = ref('creator-demo@example.com');
const credential = ref('');
const skillName = ref('PromptOps Pro');
const summary = ref('Production prompt workflow kit');
const category = ref('automation');
const priceCents = ref(9900);
const file = ref<File | null>(null);
const message = ref('');

async function handleLogin() {
  try {
    await registerUser({ username: username.value, email: email.value, password: credential.value, role: 'CREATOR' });
  } catch {
    // Existing demo account is acceptable.
  }
  const login = await loginUser({ username: username.value, password: credential.value });
  session.setSession(login);
  message.value = `已登录：${login.user.username}`;
}

function selectFile(event: Event) {
  const input = event.target as HTMLInputElement;
  file.value = input.files?.[0] ?? null;
}

async function submitSkill() {
  if (!file.value) {
    message.value = '请选择Skill文件';
    return;
  }
  const skill = await uploadSkill(session.token, {
    name: skillName.value,
    summary: summary.value,
    category: category.value,
    pricingMode: 'ONE_TIME',
    priceCents: priceCents.value,
    file: file.value
  });
  message.value = `已提交审核：${skill.name}`;
}
</script>
