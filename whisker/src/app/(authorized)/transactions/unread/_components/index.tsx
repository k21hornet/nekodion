"use client";

import { useTransition } from "react";
import { Button } from "@/components/ui/button";
import { TransactionTable } from "@/features/transaction/components/TransactionTable";
import { DailyTransactionResponse } from "@/features/transaction/types";
import { markAllAsReadAction } from "@/features/transaction/actions";
import { ArrowLeft } from "lucide-react";
import Link from "next/link";

type Props = {
  transactions: DailyTransactionResponse[];
};

export const UnreadTransactionPage = ({ transactions }: Props) => {
  const [isPending, startTransition] = useTransition();

  const ids = transactions.flatMap((d) =>
    d.dailyTransactions.map((t) => t.id),
  );

  const handleMarkAllAsRead = () => {
    startTransition(async () => {
      await markAllAsReadAction(ids);
    });
  };

  return (
    <div>
      <div className="mb-3 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Link
            href="/transactions"
            className="text-muted-foreground hover:text-foreground"
          >
            <ArrowLeft className="h-5 w-5" />
          </Link>
          <h2 className="text-xl font-semibold">
            新着入出金
            <span className="text-muted-foreground ml-1.5 text-sm font-normal">
              {ids.length}件
            </span>
          </h2>
        </div>
        <Button
          size="sm"
          variant="outline"
          onClick={handleMarkAllAsRead}
          disabled={isPending}
        >
          {isPending ? "処理中..." : "全て既読"}
        </Button>
      </div>

      <TransactionTable transactions={transactions} />
    </div>
  );
};
