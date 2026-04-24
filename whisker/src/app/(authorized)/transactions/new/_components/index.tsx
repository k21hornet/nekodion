"use client";

import { useActionState, useEffect, useRef } from "react";
import {
  createTransactionAction,
  CreateTransactionActionState,
} from "@/features/transaction/actions";
import { AccountSummaryResponse } from "@/features/accounts/types";
import { CategoryTypeResponse } from "@/features/category/types";
import { TransactionForm } from "@/features/transaction/components/TransactionForm";
import { Alert, AlertDescription } from "@/components/ui/alert";
import { CheckCircle2 } from "lucide-react";

type Props = {
  accounts: AccountSummaryResponse[];
  categories: CategoryTypeResponse[];
};

const initialState: CreateTransactionActionState = {};

export const CreateTransactionPage = ({ accounts, categories }: Props) => {
  const [state, formAction, isPending] = useActionState(
    createTransactionAction,
    initialState,
  );
  const formRef = useRef<HTMLFormElement>(null);

  useEffect(() => {
    if (state.success) {
      formRef.current?.reset();
    }
  }, [state]);

  return (
    <div className="mx-auto max-w-lg">
      <h1 className="mb-6 text-xl font-bold tracking-tight">入出金記録</h1>

      {state.success && (
        <Alert className="mb-5 border-emerald-200 bg-emerald-50 text-emerald-800">
          <CheckCircle2 className="h-4 w-4 text-emerald-600" />
          <AlertDescription>記録しました。</AlertDescription>
        </Alert>
      )}

      <TransactionForm
        formRef={formRef}
        formAction={formAction}
        isPending={isPending}
        errors={state.errors}
        accounts={accounts}
        categories={categories}
        submitLabel="記録する"
        pendingLabel="記録中..."
      />
    </div>
  );
};
