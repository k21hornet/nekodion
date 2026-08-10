import type { EmailForwardingConfirmation } from "@/features/email-forwarding/types";
import { CopyField } from "./CopyField";
import { ConfirmationCard } from "./ConfirmationCard";
import { ForwardingGuideDialog } from "./ForwardingGuideDialog";

type Props = {
  forwardingAddress: string | null;
  confirmation: EmailForwardingConfirmation | null;
};

export const SettingsPage = ({ forwardingAddress, confirmation }: Props) => {
  return (
    <div className="mx-auto max-w-2xl space-y-6 p-6">
      <h1 className="text-2xl font-semibold">設定</h1>

      {forwardingAddress && (
        <div className="border-border bg-background space-y-3 rounded-xl border p-5">
          <div>
            <p className="font-medium">カード明細の転送先アドレス</p>
            <p className="text-muted-foreground text-sm">
              カード会社からの利用明細メールをこのアドレスに転送するよう、お使いのメールクライアントで設定してください
            </p>
          </div>
          <CopyField value={forwardingAddress} />
          <ForwardingGuideDialog />
        </div>
      )}

      {confirmation && <ConfirmationCard confirmation={confirmation} />}
    </div>
  );
};
