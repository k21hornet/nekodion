import type { EmailForwardingConfirmation } from "@/features/email-forwarding/types";
import { extractConfirmationLink } from "@/features/email-forwarding/utils";
import { Button } from "@/components/ui/button";
import { CopyField } from "./CopyField";

type Props = {
  confirmation: EmailForwardingConfirmation;
};

export const ConfirmationCard = ({ confirmation }: Props) => {
  const confirmationLink = extractConfirmationLink(confirmation.bodyText);

  return (
    <div className="border-border bg-background space-y-3 rounded-xl border p-5">
      <div>
        <p className="font-medium">転送設定の確認</p>
        <p className="text-muted-foreground text-sm">
          {confirmation.fromAddress}{" "}
          から届いた転送設定の確認メールです。以下のボタンをクリックして確認を完了してください
        </p>
      </div>
      {confirmationLink ? (
        <Button asChild>
          <a href={confirmationLink} target="_blank" rel="noopener noreferrer">
            確認する
          </a>
        </Button>
      ) : (
        <CopyField value={confirmation.bodyText} />
      )}
    </div>
  );
};
