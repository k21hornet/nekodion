import { Button } from "@/components/ui/button";
import { TransactionTable } from "@/features/transaction/components/TransactionTable";
import { DailyTransactionResponse } from "@/features/transaction/types";
import { PlusCircle } from "lucide-react";
import Link from "next/link";

type Props = {
  transactions: DailyTransactionResponse[];
  unreadCount: number;
};

export const TransactionPage = ({ transactions, unreadCount }: Props) => {
  return (
    <div>
      <div className="mb-3 flex items-center justify-between">
        <h2 className="text-xl font-semibold">入出金一覧</h2>
        <Button asChild size="sm">
          <Link href="/transactions/new" className="flex items-center gap-1.5">
            <PlusCircle className="h-4 w-4" />
            記録する
          </Link>
        </Button>
      </div>

      {unreadCount > 0 && (
        <Link href="/transactions/unread">
          <div className="bg-primary/10 border-primary/20 text-primary mb-4 flex cursor-pointer items-center gap-2 rounded-lg border px-4 py-2.5 text-sm font-medium transition hover:opacity-80">
            <span className="bg-primary flex h-2 w-2 rounded-full" />
            新着 {unreadCount}件
          </div>
        </Link>
      )}

      <TransactionTable transactions={transactions} />
    </div>
  );
};
