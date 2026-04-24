import {
  ChevronRight,
  CreditCard,
  Landmark,
  Package,
  Wallet,
} from "lucide-react";
import { ACCOUNT_TYPE_LABELS } from "../../const";
import { AccountSummaryResponse } from "../../types";
import Link from "next/link";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import { Badge } from "@/components/ui/badge";

const ACCOUNT_TYPE_ICONS = {
  BANK: Landmark,
  CARD: CreditCard,
  CASH: Wallet,
  OTHER: Package,
};

const ACCOUNT_TYPE_COLORS = {
  BANK: "text-blue-600 bg-blue-50",
  CARD: "text-violet-600 bg-violet-50",
  CASH: "text-emerald-600 bg-emerald-50",
  OTHER: "text-slate-600 bg-slate-100",
};

type Props = {
  accountGroup: AccountSummaryResponse;
};

export const AccountCard = ({ accountGroup }: Props) => {
  const Icon =
    ACCOUNT_TYPE_ICONS[
      accountGroup.accountType as keyof typeof ACCOUNT_TYPE_ICONS
    ] ?? Package;
  const colorClass =
    ACCOUNT_TYPE_COLORS[
      accountGroup.accountType as keyof typeof ACCOUNT_TYPE_COLORS
    ] ?? ACCOUNT_TYPE_COLORS.OTHER;

  return (
    <Card key={accountGroup.accountType} className="overflow-hidden shadow-sm">
      <CardHeader className="pt-4 pb-2">
        <div className="flex items-center gap-2">
          <div
            className={cn(
              "flex h-7 w-7 items-center justify-center rounded-full",
              colorClass,
            )}
          >
            <Icon className="h-3.5 w-3.5" />
          </div>
          <CardTitle className="text-sm font-semibold">
            {ACCOUNT_TYPE_LABELS[accountGroup.accountType] ??
              accountGroup.accountType}
          </CardTitle>
          <Badge variant="secondary" className="text-xs">
            {accountGroup.accounts.length}
          </Badge>
        </div>
      </CardHeader>
      <CardContent className="pb-2">
        <ul className="divide-border divide-y">
          {accountGroup.accounts.map((account) => (
            <li key={account.accountId}>
              <Link
                href={`/accounts/${account.accountId}`}
                className="hover:bg-accent group -mx-1 flex items-center justify-between rounded-lg px-1 py-3 transition-colors"
              >
                <span className="group-hover:text-primary text-sm font-medium transition-colors">
                  {account.accountName}
                </span>
                <div className="flex items-center gap-1.5">
                  {accountGroup.accountType === "カード" ? (
                    <span className="text-sm font-semibold">
                      ¥{Math.abs(account.totalAmount).toLocaleString()}
                    </span>
                  ) : (
                    <span className="text-sm font-semibold">
                      ¥{account.totalAmount.toLocaleString()}
                    </span>
                  )}
                  <ChevronRight className="text-muted-foreground h-3.5 w-3.5" />
                </div>
              </Link>
            </li>
          ))}
        </ul>
      </CardContent>
    </Card>
  );
};
