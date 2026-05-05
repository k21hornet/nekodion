"use client";

import { useRef, useState, useTransition } from "react";
import { Card, CardContent } from "@/components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { ChevronLeft, ChevronRight } from "lucide-react";
import { getCategoryIconConfig } from "@/features/category/const";
import { ExpensePieChart } from "@/features/transaction/components/ExpensePieChart";
import {
  getMonthlyDataAction,
  MonthlyPageData,
} from "@/features/transaction/actions";

type MonthOption = {
  year: number;
  month: number;
  label: string;
};

function generateMonthOptions(): MonthOption[] {
  const now = new Date();
  const options: MonthOption[] = [];
  for (let i = 0; i < 13; i++) {
    const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
    options.push({
      year: d.getFullYear(),
      month: d.getMonth() + 1,
      label: `${d.getFullYear()}年${d.getMonth() + 1}月`,
    });
  }
  return options;
}

const MONTH_OPTIONS = generateMonthOptions();

type Props = { initialData: MonthlyPageData };

export const MonthlyPage = ({ initialData }: Props) => {
  const now = new Date();
  const [year, setYear] = useState(now.getFullYear());
  const [month, setMonth] = useState(now.getMonth() + 1);
  const [data, setData] = useState<MonthlyPageData>(initialData);
  const [chartKey, setChartKey] = useState(0);
  const [isPending, startTransition] = useTransition();
  const isFirstRender = useRef(true);

  const fetchMonth = (y: number, m: number) => {
    setYear(y);
    setMonth(m);
    startTransition(async () => {
      const result = await getMonthlyDataAction(y, m);
      setData(result);
      setChartKey((k) => k + 1);
    });
  };

  // 初回マウント時の重複フェッチを防ぐ
  if (isFirstRender.current) {
    isFirstRender.current = false;
  }

  const currentIndex = MONTH_OPTIONS.findIndex(
    (o) => o.year === year && o.month === month,
  );
  const canGoPrev = currentIndex < MONTH_OPTIONS.length - 1;
  const canGoNext = currentIndex > 0;

  const goToPrev = () =>
    fetchMonth(
      MONTH_OPTIONS[currentIndex + 1].year,
      MONTH_OPTIONS[currentIndex + 1].month,
    );
  const goToNext = () =>
    fetchMonth(
      MONTH_OPTIONS[currentIndex - 1].year,
      MONTH_OPTIONS[currentIndex - 1].month,
    );

  const handleSelectChange = (value: string) => {
    const [y, m] = value.split("-").map(Number);
    fetchMonth(y, m);
  };

  const summary = data.summary;
  const balance = summary.totalIncome - summary.totalExpense;

  const incomeItems = data.categoryTypeSummaries
    .filter((item) => item.isIncome)
    .sort((a, b) => b.totalAmount - a.totalAmount);

  const expenseItems = data.categoryTypeSummaries
    .filter((item) => !item.isIncome)
    .sort((a, b) => b.totalAmount - a.totalAmount);

  return (
    <div className="space-y-4">
      {/* 月選択ヘッダー */}
      <div className="flex items-center justify-between gap-2">
        <Button
          variant="ghost"
          size="icon"
          onClick={goToPrev}
          disabled={!canGoPrev || isPending}
          className="shrink-0"
        >
          <ChevronLeft className="h-5 w-5" />
        </Button>

        <Select
          value={`${year}-${month}`}
          onValueChange={handleSelectChange}
          disabled={isPending}
        >
          <SelectTrigger className="w-40 text-center font-semibold">
            <SelectValue />
          </SelectTrigger>
          <SelectContent>
            {MONTH_OPTIONS.map((o) => (
              <SelectItem
                key={`${o.year}-${o.month}`}
                value={`${o.year}-${o.month}`}
              >
                {o.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>

        <Button
          variant="ghost"
          size="icon"
          onClick={goToNext}
          disabled={!canGoNext || isPending}
          className="shrink-0"
        >
          <ChevronRight className="h-5 w-5" />
        </Button>
      </div>

      {/* データ表示（月変更中は半透明） */}
      <div className={isPending ? "opacity-50" : ""}>
        <div className="space-y-4">
          {/* 収支サマリー */}
          <Card className="shadow-sm">
            <CardContent className="py-3">
              <p className="text-muted-foreground mb-2 text-sm font-semibold">
                {month}月の収支
              </p>
              <div className="grid grid-cols-3 gap-2 text-center">
                <div>
                  <p className="text-muted-foreground text-xs">収入</p>
                  <p className="text-sm font-bold text-blue-600">
                    ¥{summary.totalIncome.toLocaleString()}
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground text-xs">支出</p>
                  <p className="text-sm font-bold text-red-500">
                    ¥{summary.totalExpense.toLocaleString()}
                  </p>
                </div>
                <div>
                  <p className="text-muted-foreground text-xs">収支</p>
                  <p
                    className={`text-sm font-bold ${
                      balance >= 0 ? "text-blue-600" : "text-red-500"
                    }`}
                  >
                    {balance >= 0 ? "+" : ""}¥{balance.toLocaleString()}
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* 収入カテゴリー種別テーブル */}
          {incomeItems.length > 0 && (
            <Card className="shadow-sm">
              <CardContent className="py-3">
                <p className="text-muted-foreground mb-3 text-sm font-semibold">
                  収入の内訳
                </p>
                <CategoryTypeTable items={incomeItems} />
              </CardContent>
            </Card>
          )}

          {/* 支出カテゴリー種別テーブル */}
          {expenseItems.length > 0 && (
            <Card className="shadow-sm">
              <CardContent className="py-3">
                <p className="text-muted-foreground mb-3 text-sm font-semibold">
                  支出の内訳
                </p>
                <ExpensePieChart key={chartKey} items={expenseItems} />
                <div className="mt-3">
                  <CategoryTypeTable items={expenseItems} />
                </div>
              </CardContent>
            </Card>
          )}

          {!isPending &&
            incomeItems.length === 0 &&
            expenseItems.length === 0 && (
              <p className="text-muted-foreground py-4 text-center text-sm">
                この月のデータはありません
              </p>
            )}
        </div>
      </div>
    </div>
  );
};

type CategoryTypeTableProps = {
  items: { categoryTypeName: string; totalAmount: number }[];
};

const CategoryTypeTable = ({ items }: CategoryTypeTableProps) => {
  return (
    <div className="space-y-2">
      {items.map((item) => {
        const { Icon, bgColor } = getCategoryIconConfig(item.categoryTypeName);
        return (
          <div key={item.categoryTypeName} className="flex items-center gap-3">
            <div
              className={`flex h-8 w-8 shrink-0 items-center justify-center rounded-full ${bgColor}`}
            >
              <Icon className="h-4 w-4 text-white" />
            </div>
            <span className="flex-1 text-sm">{item.categoryTypeName}</span>
            <span className="text-sm font-semibold">
              ¥{item.totalAmount.toLocaleString()}
            </span>
          </div>
        );
      })}
    </div>
  );
};
