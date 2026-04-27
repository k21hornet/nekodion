"use client";

import { useActionState } from "react";
import {
  updateTransactionAction,
  UpdateTransactionActionState,
  deleteTransactionAction,
} from "@/features/transaction/actions";
import { TransactionDetailResponse } from "@/features/transaction/types";
import { AccountSummaryResponse } from "@/features/accounts/types";
import { CategoryTypeResponse } from "@/features/category/types";
import { TransactionForm } from "@/features/transaction/components/TransactionForm";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { Button } from "@/components/ui/button";
import { Trash2 } from "lucide-react";

type Props = {
  transaction: TransactionDetailResponse;
  accounts: AccountSummaryResponse[];
  categories: CategoryTypeResponse[];
};

const initialState: UpdateTransactionActionState = {};

export const TransactionDetailPage = ({ transaction, accounts, categories }: Props) => {
  const [state, formAction, isPending] = useActionState(
    updateTransactionAction,
    initialState,
  );

  return (
    <div className="mx-auto max-w-lg">
      <Breadcrumb
        items={[
          { href: "/transactions", label: "入出金一覧" },
          { label: transaction.transactionName ?? "入出金詳細" },
        ]}
      />

      <h1 className="mb-6 text-xl font-bold tracking-tight">入出金編集</h1>

      <TransactionForm
        formAction={formAction}
        isPending={isPending}
        errors={state.errors}
        defaultValues={{
          transactionType: transaction.transactionType,
          accountId: String(transaction.accountId),
          categoryId: String(transaction.categoryId),
          transactionName: transaction.transactionName ?? "",
          amount: transaction.amount,
          transactionDate: transaction.transactionDateTime.split("T")[0],
          description: transaction.description ?? "",
          isAggregated: transaction.isAggregated,
        }}
        accounts={accounts}
        categories={categories}
        hiddenId={String(transaction.id)}
        submitLabel="更新する"
        pendingLabel="更新中..."
        extraActions={
          <Button
            type="button"
            variant="outline"
            disabled={isPending}
            onClick={async () => {
              if (!confirm("この入出金を削除しますか？")) return;
              await deleteTransactionAction(transaction.id);
            }}
            className="border-destructive text-destructive hover:bg-destructive/10 hover:text-destructive w-full"
          >
            <Trash2 className="mr-1.5 h-4 w-4" />
            削除する
          </Button>
        }
      />
    </div>
  );
};
