"use client";

import { useActionState } from "react";
import {
  updateAccountAction,
  UpdateAccountActionState,
  deleteAccountAction,
} from "@/features/accounts/actions";
import {
  AccountDetailResponse,
  AccountTemplateResponse,
} from "@/features/accounts/types";
import { AccountForm } from "@/features/accounts/components/AccountForm";
import { Breadcrumb } from "@/components/common/Breadcrumb";
import { Button } from "@/components/ui/button";
import { Trash2 } from "lucide-react";

type Props = {
  account: AccountDetailResponse;
  templates: AccountTemplateResponse[];
};

const updateInitialState: UpdateAccountActionState = {};

export const AccountDetailPage = ({ account, templates }: Props) => {
  const [state, formAction, isPending] = useActionState(
    updateAccountAction,
    updateInitialState,
  );

  return (
    <div className="mx-auto max-w-lg space-y-8">
      <div>
        <Breadcrumb
          items={[
            { href: "/accounts", label: "口座一覧" },
            { label: account.accountName },
          ]}
        />

        <div className="mb-6 flex items-center justify-between">
          <h1 className="text-xl font-bold tracking-tight">口座編集</h1>
          <div className="text-right">
            {account.accountType === "CARD" ? (
              <p className="text-muted-foreground text-xs">ご利用額</p>
            ) : (
              <p className="text-muted-foreground text-xs">残高</p>
            )}
            <p className="text-lg font-bold">
              {account.accountType === "CARD" ? (
                <>¥{Math.abs(account.totalAmount).toLocaleString()} 円</>
              ) : (
                <>¥{account.totalAmount.toLocaleString()}</>
              )}
            </p>
          </div>
        </div>

        <AccountForm
          formAction={formAction}
          isPending={isPending}
          errors={state.errors}
          defaultValues={{
            accountType: account.accountType,
            accountTemplateId:
              account.accountTemplateId != null
                ? String(account.accountTemplateId)
                : "",
            accountName: account.accountName,
          }}
          templates={templates}
          hiddenId={String(account.accountId)}
          submitLabel="更新する"
          pendingLabel="更新中..."
          extraActions={
            <Button
              type="button"
              variant="outline"
              disabled={isPending}
              onClick={async () => {
                if (!confirm("この口座を削除しますか？")) return;
                await deleteAccountAction(account.accountId);
              }}
              className="border-destructive text-destructive hover:bg-destructive/10 hover:text-destructive w-full"
            >
              <Trash2 className="mr-1.5 h-4 w-4" />
              削除する
            </Button>
          }
        />
      </div>
    </div>
  );
};
