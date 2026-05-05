import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

import { TransactionTable } from "@/features/transaction/components/TransactionTable";
import { ExpensePieChart } from "@/features/transaction/components/ExpensePieChart";
import {
  TotalAssetsResponse,
  DailyTransactionResponse,
  MonthlySummaryResponse,
} from "@/features/transaction/types";
import { TotalAssetsCard } from "./TotalAssetsCard";

import { ArrowRight, LogOut, PlusCircle } from "lucide-react";
import Link from "next/link";

type Props = {
  transactions: DailyTransactionResponse[];
  totalAssets: TotalAssetsResponse;
  monthlySummary: MonthlySummaryResponse;
  expenseItems: { categoryTypeName: string; totalAmount: number }[];
  initialHidden: boolean;
};

export const HomePage = ({
  transactions,
  totalAssets,
  monthlySummary,
  expenseItems,
  initialHidden,
}: Props) => {
  const limitedTransactions = transactions.slice(0, 3); // 直近日の入出金を表示
  const monthlyBalance =
    monthlySummary.totalIncome - monthlySummary.totalExpense;

  return (
    <div className="space-y-4">
      <TotalAssetsCard
        totalAssets={totalAssets}
        initialHidden={initialHidden}
      />

      <Card className="shadow-sm">
        <CardContent className="py-3">
          <p className="text-muted-foreground mb-2 text-sm font-semibold">
            {monthlySummary.month}月の収支
          </p>
          <div className="grid grid-cols-3 gap-2 text-center">
            <div>
              <p className="text-muted-foreground text-xs">収入</p>
              <p className="text-sm font-bold text-blue-600">
                ¥{monthlySummary.totalIncome.toLocaleString()}
              </p>
            </div>
            <div>
              <p className="text-muted-foreground text-xs">支出</p>
              <p className="text-sm font-bold text-red-500">
                ¥{monthlySummary.totalExpense.toLocaleString()}
              </p>
            </div>
            <div>
              <p className="text-muted-foreground text-xs">収支</p>
              <p
                className={`text-sm font-bold ${
                  monthlyBalance >= 0 ? "text-blue-600" : "text-red-500"
                }`}
              >
                {monthlyBalance >= 0 ? "+" : ""}¥
                {monthlyBalance.toLocaleString()}
              </p>
            </div>
          </div>
        </CardContent>
      </Card>

      {expenseItems.length > 0 && (
        <Card className="shadow-sm">
          <CardContent className="py-3">
            <p className="text-muted-foreground mb-2 text-sm font-semibold">
              {monthlySummary.month}月の支出内訳
            </p>
            <ExpensePieChart items={expenseItems} />
          </CardContent>
        </Card>
      )}

      <Card className="shadow-sm">
        <CardHeader className="pb-3">
          <div className="flex items-center justify-between">
            <CardTitle className="text-muted-foreground text-sm font-semibold">
              最近の入出金
            </CardTitle>
            <Button asChild variant="ghost" size="xs" className="text-primary">
              <Link
                href="/transactions/new"
                className="flex items-center gap-1"
              >
                <PlusCircle className="h-3.5 w-3.5" />
                記録する
              </Link>
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          {limitedTransactions.length > 0 ? (
            <>
              <TransactionTable transactions={limitedTransactions} />
              <div className="mt-4 text-center">
                <Button
                  asChild
                  variant="ghost"
                  size="sm"
                  className="text-primary w-full"
                >
                  <Link
                    href="/transactions"
                    className="flex items-center justify-center gap-1"
                  >
                    すべて見る
                    <ArrowRight className="h-3.5 w-3.5" />
                  </Link>
                </Button>
              </div>
            </>
          ) : (
            <p className="text-muted-foreground py-4 text-center text-sm">
              まだ入出金がありません
            </p>
          )}
        </CardContent>
      </Card>

      <div className="pt-2 pb-4 md:hidden">
        <Button
          asChild
          variant="ghost"
          size="sm"
          className="text-muted-foreground w-full"
        >
          <Link
            href="/auth/logout"
            prefetch={false}
            className="flex items-center justify-center gap-1.5"
          >
            <LogOut className="h-4 w-4" />
            ログアウト
          </Link>
        </Button>
      </div>
    </div>
  );
};
