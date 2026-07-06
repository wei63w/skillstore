<template>
  <section class="role-page">
    <p class="role-page__eyebrow">Buyer</p>
    <h1>买家前台</h1>
    <div class="flow-grid">
      <form class="flow-panel" @submit.prevent="handleLogin">
        <h2>买家登录</h2>
        <input v-model="username" placeholder="用户名" />
        <input v-model="email" placeholder="邮箱" />
        <input v-model="credential" placeholder="密码" type="password" />
        <button type="submit">注册并登录</button>
      </form>

      <div class="flow-panel">
        <h2>已上架 Skill</h2>
        <button type="button" @click="loadMarket">刷新列表</button>
        <ul>
          <li v-for="skill in skills" :key="skill.id">
            <strong>{{ skill.name }}</strong>
            <span>{{ skill.category }} · {{ formatPrice(skill.priceCents) }}</span>
            <button type="button" :disabled="!session.token" @click="buy(skill.id)">下单并支付</button>
          </li>
        </ul>
      </div>

      <div class="flow-panel">
        <h2>已购 Skill</h2>
        <button type="button" :disabled="!session.token" @click="loadPurchases">刷新已购</button>
        <ul>
          <li v-for="grant in purchases" :key="grant.id">
            <strong>{{ grant.skillId }}</strong>
            <span>下载授权：{{ grant.downloadToken }}</span>
          </li>
        </ul>
      </div>
    </div>
    <p class="flow-message">{{ message }}</p>
  </section>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { createOrder, fetchMarketSkills, fetchPurchases, loginUser, payOrder, registerUser } from '../../api/storeApi';
import type { PurchaseGrant, SkillListing } from '../../api/storeApi';
import { useSessionStore } from '../../stores/sessionStore';

const session = useSessionStore();
const username = ref('buyer-demo');
const email = ref('buyer-demo@example.com');
const credential = ref('');
const skills = ref<SkillListing[]>([]);
const purchases = ref<PurchaseGrant[]>([]);
const message = ref('');

async function handleLogin() {
  try {
    await registerUser({ username: username.value, email: email.value, password: credential.value, role: 'BUYER' });
  } catch {
    // Demo flow tolerates existing users so a refresh does not block login.
  }
  const login = await loginUser({ username: username.value, password: credential.value });
  session.setSession(login);
  message.value = `已登录：${login.user.username}`;
}

async function loadMarket() {
  try {
    skills.value = await fetchMarketSkills();
  } catch {
    skills.value = [];
    message.value = '后端未启动或暂无已上架Skill';
  }
}

async function buy(skillId: string) {
  const order = await createOrder(session.token, skillId);
  const paid = await payOrder(session.token, order.id, 'SUCCESS');
  message.value = `支付完成：${paid.orderNo}`;
  await loadPurchases();
}

async function loadPurchases() {
  purchases.value = await fetchPurchases(session.token);
}

function formatPrice(value: number) {
  return value === 0 ? '免费' : `￥${(value / 100).toFixed(2)}`;
}

if (import.meta.env.MODE !== 'test') {
  onMounted(loadMarket);
}
</script>
