<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { usePipelineStore } from './stores/pipelineStore';

const store = usePipelineStore();
const providerKey = ref('stub');
const dryRun = ref(true);
const rightPanel = ref<'logs' | 'approval' | 'diff'>('logs');

const activeProvider = computed(() => {
  return store.providers.find((provider) => provider.providerKey === providerKey.value);
});

onMounted(async () => {
  await store.refresh();
  providerKey.value = store.selectedProvider;
});
</script>

<template>
  <main class="console-shell">
    <aside class="run-list" aria-label="任务列表">
      <div class="toolbar">
        <h1>Harness Console</h1>
        <button class="icon-button" title="刷新" @click="store.refresh">↻</button>
      </div>

      <label class="field">
        <span>Provider</span>
        <select v-model="providerKey">
          <option v-for="provider in store.providers" :key="provider.providerKey" :value="provider.providerKey">
            {{ provider.providerKey }} {{ provider.configured ? '' : '(未配置)' }}
          </option>
        </select>
      </label>

      <label class="toggle">
        <input v-model="dryRun" type="checkbox" />
        <span>Dry-run</span>
      </label>

      <button class="primary" :disabled="store.loading" @click="store.start(providerKey, dryRun)">
        启动 Pipeline
      </button>

      <p v-if="activeProvider" class="provider-hint">{{ activeProvider.message }}</p>
      <p v-if="store.error" class="error">{{ store.error }}</p>

      <div class="runs">
        <button
          v-for="run in store.runs"
          :key="run.runId"
          class="run-item"
          :class="{ selected: store.selectedRun?.runId === run.runId }"
          @click="store.selectRun(run.runId)"
        >
          <strong>{{ run.runId }}</strong>
          <span>{{ run.status }} · {{ run.currentStage }}</span>
        </button>
        <p v-if="store.runs.length === 0" class="empty">暂无 pipeline run</p>
      </div>
    </aside>

    <section class="stage-panel" aria-label="Pipeline 阶段">
      <header class="panel-header">
        <div>
          <p class="eyebrow">Pipeline</p>
          <h2>{{ store.selectedRun?.runId ?? '未选择任务' }}</h2>
        </div>
        <span class="status-pill">{{ store.selectedRun?.status ?? 'EMPTY' }}</span>
      </header>

      <ol class="stage-list">
        <li v-for="stage in store.selectedStages" :key="stage.stageId" class="stage-row">
          <span class="stage-dot" :data-status="stage.status"></span>
          <div>
            <strong>{{ stage.stageType }}</strong>
            <p>{{ stage.status }} <span v-if="stage.failureSummary">· {{ stage.failureSummary }}</span></p>
          </div>
        </li>
      </ol>
      <p v-if="store.selectedStages.length === 0" class="empty center">选择或启动一个 pipeline run</p>
    </section>

    <section class="detail-panel" aria-label="日志审批和 diff">
      <nav class="tabs">
        <button :class="{ active: rightPanel === 'logs' }" @click="rightPanel = 'logs'">日志</button>
        <button :class="{ active: rightPanel === 'approval' }" @click="rightPanel = 'approval'">审批</button>
        <button :class="{ active: rightPanel === 'diff' }" @click="rightPanel = 'diff'">Diff</button>
      </nav>

      <pre v-if="rightPanel === 'logs'" class="log-view">{{ store.logs || '暂无日志' }}</pre>
      <div v-else-if="rightPanel === 'approval'" class="approval-view">
        <h3>待审批动作</h3>
        <p>部署、端口、权限、Git push 和未解决高危问题默认需要人工确认。</p>
        <button disabled>批准</button>
        <button disabled>拒绝</button>
      </div>
      <div v-else class="diff-view">
        <h3>Diff Review</h3>
        <p>模型补丁会在 Schema 校验后进入 diff review。当前 run 的 diff 证据请查看阶段日志。</p>
      </div>
    </section>
  </main>
</template>
