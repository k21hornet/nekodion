import { formatAmount } from "@/util/formatAmount";
import { formatDate } from "@/util/formatDate";
import { DailyTransactionResponse } from "../../types";
import Link from "next/link";
import { cn } from "@/lib/utils";

type Props = {
  transactions: DailyTransactionResponse[];
};

export const TransactionTable = ({ transactions }: Props) => {
  return (
    <div className="space-y-4">
      {transactions.map((group) => (
        <div key={group.transactionDateTime}>
          <p className="text-muted-foreground border-border border-b pb-2 text-xs font-medium">
            {formatDate(group.transactionDateTime)}
          </p>
          <ul className="mt-1">
            {group.dailyTransactions.map((tx) => (
              <li key={tx.id}>
                <Link
                  href={`/transactions/${tx.id}`}
                  className="hover:bg-accent group flex items-center justify-between rounded-lg px-1 py-2.5 transition-colors"
                >
                  <span className="group-hover:text-primary min-w-0 flex-1 truncate text-sm font-medium transition-colors">
                    {tx.transactionName ?? "（名称なし）"}
                  </span>
                  <span
                    className={cn(
                      "ml-3 shrink-0 text-sm font-semibold",
                      tx.transactionType === "INCOME"
                        ? "text-emerald-600"
                        : "text-rose-500",
                    )}
                  >
                    {formatAmount(tx.amount, tx.transactionType)}
                  </span>
                </Link>
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
};
