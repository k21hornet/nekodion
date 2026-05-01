import { getUnreadTransactions } from "@/features/transaction/api";
import { UnreadTransactionPage } from "./_components";

export default async function UnreadTransactions() {
  const response = await getUnreadTransactions();

  if ("error" in response) {
    throw new Error("データの取得に失敗しました");
  }

  return <UnreadTransactionPage transactions={response.body} />;
}
