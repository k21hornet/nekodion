"use client";

import { useState } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Switch } from "@/components/ui/switch";
import { FormField } from "@/components/common/FormField";
import { AccountSummaryResponse } from "@/features/accounts/types";
import { ACCOUNT_TYPE_LABELS } from "@/features/accounts/const";
import { CategoryTypeResponse } from "@/features/category/types";

type FormErrors = {
  global?: string;
  transactionType?: string;
  accountId?: string;
  categoryId?: string;
  transactionName?: string;
  amount?: string;
  transactionDate?: string;
};

type Props = {
  formAction: (payload: FormData) => void;
  isPending: boolean;
  errors?: FormErrors;
  defaultValues?: {
    transactionType?: string;
    accountId?: string;
    categoryId?: string;
    transactionName?: string;
    amount?: number;
    transactionDate?: string;
    description?: string;
    isAggregated?: boolean;
  };
  accounts: AccountSummaryResponse[];
  categories: CategoryTypeResponse[];
  submitLabel: string;
  pendingLabel: string;
  extraActions?: React.ReactNode;
  formRef?: React.RefObject<HTMLFormElement | null>;
  hiddenId?: string;
};

const today = new Date().toISOString().split("T")[0];

export const TransactionForm = ({
  formAction,
  isPending,
  errors,
  defaultValues,
  accounts,
  categories,
  submitLabel,
  pendingLabel,
  extraActions,
  formRef,
  hiddenId,
}: Props) => {
  const [selectedType, setSelectedType] = useState(
    defaultValues?.transactionType ?? "",
  );
  const [isAggregated, setIsAggregated] = useState(
    defaultValues?.isAggregated ?? true,
  );

  const isIncome = selectedType === "INCOME";
  const visibleCategories = categories.filter((ct) => ct.isIncome === isIncome);

  return (
    <form ref={formRef} action={formAction} className="space-y-5">
      {hiddenId && <input type="hidden" name="id" value={hiddenId} />}
      {errors?.global && (
        <div className="bg-destructive/10 border-destructive/20 text-destructive rounded-lg border px-4 py-3 text-sm">
          {errors.global}
        </div>
      )}

      <FormField label="取引種別" error={errors?.transactionType}>
        <select
          name="transactionType"
          defaultValue={defaultValues?.transactionType ?? ""}
          onChange={(e) => setSelectedType(e.target.value)}
          className="border-input bg-background focus:ring-ring w-full rounded-lg border px-3 py-2 text-sm transition focus:border-transparent focus:ring-2 focus:outline-none"
        >
          <option value="">選択してください</option>
          <option value="INCOME">収入</option>
          <option value="EXPENSE">支出</option>
        </select>
      </FormField>

      <FormField label="カテゴリー" error={errors?.categoryId}>
        <select
          name="categoryId"
          defaultValue={defaultValues?.categoryId ?? ""}
          key={selectedType}
          className="border-input bg-background focus:ring-ring w-full rounded-lg border px-3 py-2 text-sm transition focus:border-transparent focus:ring-2 focus:outline-none"
        >
          <option value="">
            {selectedType ? "選択してください" : "取引種別を先に選択してください"}
          </option>
          {visibleCategories.map((ct) => (
            <optgroup key={ct.categoryTypeId} label={ct.categoryTypeName}>
              {ct.categories.map((c) => (
                <option key={c.categoryId} value={c.categoryId}>
                  {c.categoryName}
                </option>
              ))}
            </optgroup>
          ))}
        </select>
      </FormField>

      <FormField label="口座" error={errors?.accountId}>
        <select
          name="accountId"
          defaultValue={defaultValues?.accountId ?? ""}
          className="border-input bg-background focus:ring-ring w-full rounded-lg border px-3 py-2 text-sm transition focus:border-transparent focus:ring-2 focus:outline-none"
        >
          <option value="">選択してください</option>
          {accounts.map((group) => (
            <optgroup
              key={group.accountType}
              label={
                ACCOUNT_TYPE_LABELS[group.accountType] ?? group.accountType
              }
            >
              {group.accounts.map((a) => (
                <option key={a.accountId} value={a.accountId}>
                  {a.accountName}
                </option>
              ))}
            </optgroup>
          ))}
        </select>
      </FormField>

      <FormField label="取引名" error={errors?.transactionName}>
        <Input
          type="text"
          name="transactionName"
          placeholder="例: ランチ代"
          maxLength={100}
          defaultValue={defaultValues?.transactionName ?? ""}
        />
      </FormField>

      <FormField label="金額" error={errors?.amount}>
        <Input
          type="number"
          name="amount"
          placeholder="0"
          min={1}
          defaultValue={defaultValues?.amount ?? ""}
        />
      </FormField>

      <FormField label="取引日" error={errors?.transactionDate}>
        <Input
          type="date"
          name="transactionDate"
          defaultValue={defaultValues?.transactionDate ?? today}
        />
      </FormField>

      <FormField label="メモ" optional>
        <Input
          type="text"
          name="description"
          placeholder="例: 同僚と"
          maxLength={255}
          defaultValue={defaultValues?.description ?? ""}
        />
      </FormField>

      <div className="flex items-center justify-between">
        <span className="text-sm font-medium">計算対象</span>
        <div className="flex items-center gap-2">
          <Switch
            checked={isAggregated}
            onCheckedChange={setIsAggregated}
          />
          <span className="text-muted-foreground text-sm">
            {isAggregated ? "ON" : "OFF"}
          </span>
        </div>
      </div>
      <input type="hidden" name="isAggregated" value={isAggregated ? "true" : "false"} />

      <div className="flex flex-col gap-3 pt-1">
        <Button type="submit" disabled={isPending} className="w-full">
          {isPending ? pendingLabel : submitLabel}
        </Button>
        {extraActions}
      </div>
    </form>
  );
};
