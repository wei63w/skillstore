import { defineStore } from 'pinia';
import { harnessApi, type PipelineRun, type ProviderStatus } from '../api/harnessApi';

export const usePipelineStore = defineStore('pipeline', {
  state: () => ({
    providers: [] as ProviderStatus[],
    runs: [] as PipelineRun[],
    selectedRun: null as PipelineRun | null,
    logs: '',
    loading: false,
    error: ''
  }),
  getters: {
    selectedStages: (state) => state.selectedRun?.stages ?? [],
    selectedProvider: (state) => state.providers.find((provider) => provider.configured)?.providerKey ?? 'stub'
  },
  actions: {
    async refresh() {
      this.loading = true;
      this.error = '';
      try {
        const [providers, runs] = await Promise.all([harnessApi.listProviders(), harnessApi.listRuns()]);
        this.providers = providers;
        this.runs = runs;
        if (!this.selectedRun && runs.length > 0) {
          await this.selectRun(runs[0].runId);
        }
      } catch (error) {
        this.error = error instanceof Error ? error.message : '刷新失败';
      } finally {
        this.loading = false;
      }
    },
    async selectRun(runId: string) {
      this.loading = true;
      this.error = '';
      try {
        this.selectedRun = await harnessApi.getRun(runId);
        this.logs = await harnessApi.getLogs(runId);
      } catch (error) {
        this.error = error instanceof Error ? error.message : '读取 run 失败';
      } finally {
        this.loading = false;
      }
    },
    async start(providerKey: string, dryRun: boolean) {
      this.loading = true;
      this.error = '';
      try {
        const run = await harnessApi.startPipeline(providerKey, dryRun);
        this.selectedRun = run;
        this.logs = await harnessApi.getLogs(run.runId);
        await this.refresh();
      } catch (error) {
        this.error = error instanceof Error ? error.message : '启动 pipeline 失败';
      } finally {
        this.loading = false;
      }
    }
  }
});
