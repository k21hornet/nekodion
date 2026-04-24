import { CreateTransactionPage } from "./_components";
import { getAccounts } from "@/features/accounts/api";
import { getCategories } from "@/features/category/api";

export default async function CreateTransaction() {
  const [accountsResponse, categoriesResponse] = await Promise.all([
    getAccounts(),
    getCategories(),
  ]);

  if ("error" in accountsResponse) {
    throw new Error("データの取得に失敗しました");
  }
  if ("error" in categoriesResponse) {
    throw new Error("データの取得に失敗しました");
  }

  return (
    <CreateTransactionPage
      accounts={accountsResponse.body}
      categories={categoriesResponse.body}
    />
  );
}
