import { getTransactions, getUnreadCount } from "@/features/transaction/api";
import { TransactionPage } from "./_components";
import { UnreadCountResponse } from "@/features/transaction/types";

export default async function Transaction() {
  const [transactionsResponse, unreadCountResponse] = await Promise.all([
    getTransactions(),
    getUnreadCount(),
  ]);

  if ("error" in transactionsResponse) {
    throw new Error("データの取得に失敗しました");
  }

  const unreadCount =
    "error" in unreadCountResponse
      ? 0
      : (unreadCountResponse.body as UnreadCountResponse).count;

  return (
    <TransactionPage
      transactions={transactionsResponse.body}
      unreadCount={unreadCount}
    />
  );
}
