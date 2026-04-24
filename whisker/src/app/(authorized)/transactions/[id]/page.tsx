import { getTransaction } from "@/features/transaction/api";
import { getAccounts } from "@/features/accounts/api";
import { getCategories } from "@/features/category/api";
import { TransactionDetailPage } from "./_components";

type Props = {
  params: Promise<{ id: string }>;
};

export default async function TransactionDetail({ params }: Props) {
  const { id } = await params;
  const [transactionResponse, accountsResponse, categoriesResponse] =
    await Promise.all([
      getTransaction(Number(id)),
      getAccounts(),
      getCategories(),
    ]);

  if ("error" in transactionResponse) {
    throw new Error("データの取得に失敗しました");
  }
  if ("error" in accountsResponse) {
    throw new Error("データの取得に失敗しました");
  }
  if ("error" in categoriesResponse) {
    throw new Error("データの取得に失敗しました");
  }

  return (
    <TransactionDetailPage
      transaction={transactionResponse.body}
      accounts={accountsResponse.body}
      categories={categoriesResponse.body}
    />
  );
}
