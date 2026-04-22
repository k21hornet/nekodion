"use server";

import { fetcher } from "@/util/fetcher";
import { redirect } from "next/navigation";

export type CreateTransactionActionState = {
  success?: boolean;
  errors?: {
    transactionType?: string;
    accountId?: string;
    amount?: string;
    transactionDate?: string;
    global?: string;
  };
};

export async function createTransactionAction(
  _prev: CreateTransactionActionState,
  formData: FormData,
): Promise<CreateTransactionActionState> {
  const transactionType = formData.get("transactionType") as string;
  const accountId = formData.get("accountId") as string;
  const transactionName =
    (formData.get("transactionName") as string)?.trim() || null;
  const amountStr = formData.get("amount") as string;
  const transactionDate = formData.get("transactionDate") as string;
  const description = (formData.get("description") as string)?.trim() || null;

  const errors: CreateTransactionActionState["errors"] = {};
  if (!transactionType) errors.transactionType = "取引種別を選択してください";
  if (!accountId) errors.accountId = "口座を選択してください";
  if (!amountStr || Number(amountStr) <= 0)
    errors.amount = "1以上の金額を入力してください";
  if (!transactionDate) errors.transactionDate = "取引日を入力してください";
  if (Object.keys(errors).length > 0) return { errors };

  const result = await fetcher.post("/transactions", {
    transactionType,
    accountId: Number(accountId),
    transactionName,
    amount: Number(amountStr),
    transactionDateTime: `${transactionDate}T00:00:00`,
    description,
  });

  if ("error" in result) {
    return { errors: { global: "入出金の記録に失敗しました" } };
  }

  return { success: true };
}

export type UpdateTransactionActionState = {
  errors?: {
    transactionType?: string;
    accountId?: string;
    amount?: string;
    transactionDate?: string;
    global?: string;
  };
};

export async function updateTransactionAction(
  _prev: UpdateTransactionActionState,
  formData: FormData,
): Promise<UpdateTransactionActionState> {
  const id = formData.get("id") as string;
  const transactionType = formData.get("transactionType") as string;
  const accountId = formData.get("accountId") as string;
  const transactionName =
    (formData.get("transactionName") as string)?.trim() || null;
  const amountStr = formData.get("amount") as string;
  const transactionDate = formData.get("transactionDate") as string;
  const description = (formData.get("description") as string)?.trim() || null;

  const errors: UpdateTransactionActionState["errors"] = {};
  if (!transactionType) errors.transactionType = "取引種別を選択してください";
  if (!accountId) errors.accountId = "口座を選択してください";
  if (!amountStr || Number(amountStr) <= 0)
    errors.amount = "1以上の金額を入力してください";
  if (!transactionDate) errors.transactionDate = "取引日を入力してください";
  if (Object.keys(errors).length > 0) return { errors };

  const result = await fetcher.put(`/transactions/${id}`, {
    transactionType,
    accountId: Number(accountId),
    transactionName,
    amount: Number(amountStr),
    transactionDateTime: `${transactionDate}T00:00:00`,
    description,
  });

  if ("error" in result) {
    return { errors: { global: "入出金の更新に失敗しました" } };
  }

  redirect("/transactions");
}

export async function deleteTransactionAction(id: number): Promise<void> {
  await fetcher.delete(`/transactions/${id}`);
  redirect("/transactions");
}
