"use client";

import { useEffect, useState, useTransition } from "react";
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

export const MonthlyPage = () => {
  const now = new Date();
  const [year, setYear] = useState(now.getFullYear());
  const [month, setMonth] = useState(now.getMonth() + 1);
  const [data, setData] = useState<MonthlyPageData | null>(null);
  const [isPending, startTransition] = useTransition();

  useEffect(() => {
    startTransition(async () => {
      const result = await getMonthlyDataAction(year, month);
      setData(result);
    });
  }, [year, month]);

  const currentIndex = MONTH_OPTIONS.findIndex(
    (o) => o.year === year && o.month === month,
  );
  const canGoPrev = currentIndex < MONTH_OPTIONS.length - 1;
  const canGoNext = currentIndex > 0;

  const goToPrev = () => {
    const prev = MONTH_OPTIONS[currentIndex + 1];
    setYear(prev.year);
    setMonth(prev.month);
  };

  const goToNext = () => {
    const next = MONTH_OPTIONS[currentIndex - 1];
    setYear(next.year);
    setMonth(next.month);
  };

  const handleSelectChange = (value: string) => {
    const [y, m] = value.split("-").map(Number);
    setYear(y);
    setMonth(m);
  };

  const summary = data?.summary;
  const balance = summary
    ? summary.totalIncome - summary.totalExpense
    : 0;

  const incomeItems = (data?.categoryTypeSummaries ?? [])
    .filter((item) => item.isIncome)
    .sort((a, b) => b.totalAmount - a.totalAmount);

  const expenseItems = (data?.categoryTypeSummaries ?? [])
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
              <SelectItem key={`${o.year}-${o.month}`} value={`${o.year}-${o.month}`}>
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

      {/* 収支サマリー */}
      <Card className="shadow-sm">
        <CardContent className="py-3">
          <p className="text-sm font-semibold text-muted-foreground mb-2">
            {month}月の収支
          </p>
          <div className="grid grid-cols-3 gap-2 text-center">
            <div>
              <p className="text-xs text-muted-foreground">収入</p>
              <p className="text-sm font-bold text-blue-600">
                ¥{(summary?.totalIncome ?? 0).toLocaleString()}
              </p>
            </div>
            <div>
              <p className="text-xs text-muted-foreground">支出</p>
              <p className="text-sm font-bold text-red-500">
                ¥{(summary?.totalExpense ?? 0).toLocaleString()}
              </p>
            </div>
            <div>
              <p className="text-xs text-muted-foreground">収支</p>
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
            <p className="text-sm font-semibold text-muted-foreground mb-3">
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
            <p className="text-sm font-semibold text-muted-foreground mb-3">
              支出の内訳
            </p>
            <CategoryTypeTable items={expenseItems} />
          </CardContent>
        </Card>
      )}

      {!isPending && data && incomeItems.length === 0 && expenseItems.length === 0 && (
        <p className="text-muted-foreground py-4 text-center text-sm">
          この月のデータはありません
        </p>
      )}
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
          <div
            key={item.categoryTypeName}
            className="flex items-center gap-3"
          >
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
