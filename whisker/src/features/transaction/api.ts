import { fetcher } from "@/util/fetcher";

export async function getTransactions() {
  return await fetcher.get("/transactions");
}

export async function getTotalAssets() {
  return await fetcher.get("/transactions/total-assets");
}

export async function getMonthlySummary(year: number, month: number) {
  return await fetcher.get(
    `/transactions/monthly-summary?year=${year}&month=${month}`,
  );
}

export async function getTransaction(id: number) {
  return await fetcher.get(`/transactions/${id}`);
}

export async function getUnreadTransactions() {
  return await fetcher.get("/transactions/unread");
}

export async function getUnreadCount() {
  return await fetcher.get("/transactions/unread-count");
}

export async function markAllAsRead() {
  return await fetcher.patch("/transactions/read-all", {});
}
