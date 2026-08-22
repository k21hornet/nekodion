<p align="center">
  <img src="scratch/img/app-icon.png" width="120" alt="Nekodion" />
</p>

<h1 align="center">Nekodion</h1>

<p align="center">
  <img src="https://img.shields.io/badge/%23cat-000?style=flat" alt="#cat" />
  <img src="https://img.shields.io/badge/%23konekokonekone-000?style=flat" alt="#konekokonekone" />
</p>

## 子猫こねこね

個人家計簿アプリ。収支の記録・管理をシンプルに行えるほか、口座やクレカのご利用通知メール（Gmail）から自動で入出金を取り込む機能を備える。

### 主な機能

- **入出金管理** — 収入・支出の記録、一覧表示、編集・削除
- **カテゴリー管理** — 収入・支出ごとにカテゴリーを分類
- **口座管理** — 銀行・カード・現金・その他の口座を管理
- **月次収支** — 月ごとの収入・支出・収支を集計表示
- **総資産確認** — 全口座の資産合計をリアルタイムで把握
- **メール自動取込** — 口座入出金通知・カードのご利用通知メールから取引を自動インポート

### Gmail自動インポート対象

- 口座
  - 三井住友銀行
- クレカ
  - 三井住友カード
  - JCBカード

## モジュール構成

```
nekodion
├─ fluffy       # DB
├─ knead        # iOS
├─ pounce       # スクリプト
├─ prowl        # バックエンド
├─ scratch      # docs
├─ whisker      # フロントエンド
└─ README.md
```

## ブランチ戦略

- 基本mainにマージ
- でかいやつはfeatureブランチで作業
