"use client";

import { useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Eye, EyeOff } from "lucide-react";
import { TotalAssetsResponse } from "@/features/transaction/types";

type Props = {
  totalAssets: TotalAssetsResponse;
  initialHidden: boolean;
};

const COOKIE_NAME = "totalAssetsHidden";

export const TotalAssetsCard = ({ totalAssets, initialHidden }: Props) => {
  const [hidden, setHidden] = useState(initialHidden);

  const toggle = () => {
    const next = !hidden;
    setHidden(next);
    document.cookie = `${COOKIE_NAME}=${next}; path=/; max-age=${60 * 60 * 24 * 365}`;
  };

  return (
    <Card className="border-0 shadow-sm">
      <CardContent className="py-3">
        <div className="flex items-center justify-between">
          <p className="text-xl font-bold">総資産</p>
          <button
            onClick={toggle}
            className="text-muted-foreground"
            aria-label="総資産の表示切替"
          >
            {hidden ? (
              <EyeOff className="h-5 w-5" />
            ) : (
              <Eye className="h-5 w-5" />
            )}
          </button>
        </div>
        <p className="text-right text-3xl font-bold">
          {hidden
            ? "¥ ---,---"
            : `¥${totalAssets.totalAssets.toLocaleString()}`}
        </p>
      </CardContent>
    </Card>
  );
};
