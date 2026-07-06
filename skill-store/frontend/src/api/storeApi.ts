import axios from 'axios';
import type { StoreRole, StoreSession, StoreUser } from '../stores/sessionStore';

export const storeApi = axios.create({
  baseURL: '/api/store',
  timeout: 10000
});

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string | null;
  timestamp: string;
}

export interface SkillListing {
  id: string;
  name: string;
  summary: string;
  category: string;
  pricingMode: 'FREE' | 'ONE_TIME' | 'SUBSCRIPTION';
  priceCents: number;
  auditStatus: 'PENDING_REVIEW' | 'APPROVED' | 'REJECTED';
  reviewReason: string | null;
}

export interface StoreOrder {
  id: string;
  orderNo: string;
  skillId: string;
  amountCents: number;
  paymentStatus: 'PENDING' | 'PAID' | 'FAILED';
  grant: PurchaseGrant | null;
}

export interface PurchaseGrant {
  id: string;
  skillId: string;
  orderId: string;
  downloadToken: string;
}

const authHeaders = (token: string) => ({
  headers: {
    'X-Store-Token': token
  }
});

export async function registerUser(payload: {
  username: string;
  email: string;
  password: string;
  role: StoreRole;
}) {
  const response = await storeApi.post<ApiResponse<StoreUser>>('/auth/register', payload);
  return response.data.data;
}

export async function loginUser(payload: { username: string; password: string }) {
  const response = await storeApi.post<ApiResponse<StoreSession>>('/auth/login', payload);
  return response.data.data;
}

export async function uploadSkill(token: string, payload: {
  name: string;
  summary: string;
  category: string;
  pricingMode: SkillListing['pricingMode'];
  priceCents: number;
  file: File;
}) {
  const formData = new FormData();
  formData.append('name', payload.name);
  formData.append('summary', payload.summary);
  formData.append('category', payload.category);
  formData.append('pricingMode', payload.pricingMode);
  formData.append('priceCents', String(payload.priceCents));
  formData.append('file', payload.file);
  const response = await storeApi.post<ApiResponse<SkillListing>>('/creator/skills', formData, authHeaders(token));
  return response.data.data;
}

export async function fetchPendingReviews(token: string) {
  const response = await storeApi.get<ApiResponse<SkillListing[]>>('/admin/reviews/pending', authHeaders(token));
  return response.data.data;
}

export async function reviewSkill(token: string, skillId: string, decision: 'APPROVE' | 'REJECT', reason: string) {
  const response = await storeApi.post<ApiResponse<SkillListing>>(
    `/admin/reviews/${skillId}`,
    { decision, reason },
    authHeaders(token)
  );
  return response.data.data;
}

export async function fetchMarketSkills() {
  const response = await storeApi.get<ApiResponse<SkillListing[]>>('/market/skills');
  return response.data.data;
}

export async function createOrder(token: string, skillId: string) {
  const response = await storeApi.post<ApiResponse<StoreOrder>>('/orders', { skillId }, authHeaders(token));
  return response.data.data;
}

export async function payOrder(token: string, orderId: string, result: 'SUCCESS' | 'FAIL') {
  const response = await storeApi.post<ApiResponse<StoreOrder>>(`/orders/${orderId}/pay`, { result }, authHeaders(token));
  return response.data.data;
}

export async function fetchPurchases(token: string) {
  const response = await storeApi.get<ApiResponse<PurchaseGrant[]>>('/buyer/purchases', authHeaders(token));
  return response.data.data;
}
