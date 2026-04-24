import {
  getTotalAssets,
  getTransactions,
  getMonthlySummary,
} from "@/features/transaction/api";
import { HomePage } from "./_components";

export default async function Home() {
  const now = new Date();
  const year = now.getFullYear();
  const month = now.getMonth() + 1;

  const [transactionResponse, totalAssetsResponse, monthlySummaryResponse] =
    await Promise.all([
      getTransactions(),
      getTotalAssets(),
      getMonthlySummary(year, month),
    ]);

  if (
    "error" in transactionResponse ||
    "error" in totalAssetsResponse ||
    "error" in monthlySummaryResponse
  ) {
    throw new Error("データの取得に失敗しました");
  }

  return (
    <HomePage
      transactions={transactionResponse.body}
      totalAssets={totalAssetsResponse.body}
      monthlySummary={monthlySummaryResponse.body}
    />
  );
}
