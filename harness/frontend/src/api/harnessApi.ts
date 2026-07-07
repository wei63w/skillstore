import axios from 'axios';

export type ProviderStatus = {
  providerKey: string;
  modelName: string;
  endpoint: string;
  secretEnv: string;
  enabled: boolean;
  configured: boolean;
  message: string;
};

export type PipelineStage = {
  stageId: string;
  runId: string;
  stageType: string;
  status: string;
  startedAt: string;
  finishedAt: string;
  evidencePath: string;
  failureSummary?: string;
};

export type PipelineRun = {
  runId: string;
  featureDirectory: string;
  targetWorkspace: string;
  status: string;
  currentStage: string;
  repairCount: number;
  reportPath: string;
  stages: PipelineStage[];
};

type ApiResponse<T> = {
  success: boolean;
  data: T;
  message?: string;
};

const client = axios.create({
  baseURL: import.meta.env.VITE_HARNESS_API_BASE ?? '',
  timeout: 30000
});

async function unwrap<T>(promise: Promise<{ data: ApiResponse<T> }>): Promise<T> {
  const response = await promise;
  if (!response.data.success) {
    throw new Error(response.data.message ?? 'Harness API 调用失败');
  }
  return response.data.data;
}

export const harnessApi = {
  listProviders() {
    return unwrap<ProviderStatus[]>(client.get('/api/harness/model-providers'));
  },
  listRuns() {
    return unwrap<PipelineRun[]>(client.get('/api/harness/pipelines'));
  },
  getRun(runId: string) {
    return unwrap<PipelineRun>(client.get(`/api/harness/pipelines/${runId}`));
  },
  getLogs(runId: string) {
    return unwrap<string>(client.get(`/api/harness/pipelines/${runId}/logs`));
  },
  startPipeline(providerKey: string, dryRun: boolean) {
    return unwrap<PipelineRun>(
      client.post('/api/harness/pipelines', {
        providerKey,
        dryRun,
        featureDirectory: 'specs/007-real-harness-console'
      })
    );
  }
};
