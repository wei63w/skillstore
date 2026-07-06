<template>
  <div class="store-shell">
    <aside class="store-shell__nav">
      <strong>OpenClaw Skill 商店</strong>
      <RouterLink to="/">买家前台</RouterLink>
      <RouterLink to="/creator">创作者后台</RouterLink>
      <RouterLink to="/admin">平台运营后台</RouterLink>
    </aside>
    <main class="store-shell__main">
      <RouterView />
    </main>
  </div>
</template>
