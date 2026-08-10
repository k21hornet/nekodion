import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";

const steps = [
  {
    title: "転送先アドレスをコピーする",
    body: "この画面の上に表示されている「カード明細の転送先アドレス」をコピーボタンでコピーします。",
  },
  {
    title: "Gmailの設定画面を開く",
    body: "PCのブラウザでGmailを開き、右上の歯車アイコン（⚙️）→「すべての設定を表示」をクリックします。",
  },
  {
    title: "「メール転送とPOP/IMAP」タブを開く",
    body: "設定画面の上部タブから「メール転送とPOP/IMAP」を選びます。",
  },
  {
    title: "転送先アドレスを追加する",
    body: "「転送先アドレスを追加」ボタンをクリックし、手順1でコピーしたアドレスを貼り付けて「次へ」→「続行」を押します。",
  },
  {
    title: "転送設定を確認する",
    body: "しばらくすると、この設定画面の下部に「転送設定の確認」が表示されます。「確認する」ボタンをクリックし、Gmailの確認画面で転送を承認してください。",
  },
  {
    title: "カード明細メールだけ転送するフィルタを作る",
    body: "Gmailの検索バー右端のスライダーのアイコンをクリックし、「From」欄にカード会社のメールアドレス（例: vpass.ne.jp、jcb.co.jp など）を入力します。「フィルタを作成」→「転送先」にチェックを入れ、手順4で追加した転送先アドレスを選んで「フィルタを作成」を押せば完了です（転送先メールは一つずつ追加する必要があります）。他のメールは転送されません。",
  },
];

export const ForwardingGuideDialog = () => {
  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button type="button" variant="link" size="sm" className="px-0">
          Gmailでの転送設定のやり方を見る
        </Button>
      </DialogTrigger>
      <DialogContent className="max-h-[80vh] overflow-y-auto sm:max-w-md">
        <DialogHeader>
          <DialogTitle>Gmailでの転送設定のやり方</DialogTitle>
        </DialogHeader>
        <ol className="space-y-4">
          {steps.map((step, index) => (
            <li key={step.title} className="flex gap-3">
              <span className="bg-muted text-muted-foreground flex size-6 shrink-0 items-center justify-center rounded-full text-xs font-medium">
                {index + 1}
              </span>
              <div className="space-y-1">
                <p className="text-sm font-medium">{step.title}</p>
                <p className="text-muted-foreground text-sm">{step.body}</p>
              </div>
            </li>
          ))}
        </ol>
      </DialogContent>
    </Dialog>
  );
};
