# iOS設計

対象: `knead/Nekodion`（SwiftUIアプリ、バックエンド `prowl/api` を利用するクライアント）

## ディレクトリ構成

```
knead
├─ Nekodion.xcodeproj
├─ Nekodion
│  ├─ App
│  │  ├─ NekodionApp.swift        # @main
│  │  └─ RootView.swift           # 未認証/認証済みの出し分け、TabView
│  ├─ Core
│  │  ├─ Auth
│  │  │  └─ AuthenticationService.swift
│  │  ├─ Networking
│  │  │  └─ APIClient.swift       # 汎用HTTPクライアント（fetcherに相当）
│  │  └─ Config
│  │     └─ AppConfig.swift       # API_BASE_URL 等、xcconfig経由で注入
│  ├─ Features
│  │  ├─ Home
│  │  │  ├─ HomeView.swift
│  │  │  └─ HomeViewModel.swift
│  │  ├─ Transaction
│  │  │  ├─ TransactionListView.swift
│  │  │  ├─ TransactionDetailView.swift
│  │  │  ├─ TransactionFormView.swift
│  │  │  ├─ TransactionViewModel.swift
│  │  │  ├─ TransactionAPI.swift
│  │  │  └─ TransactionModels.swift
│  │  ├─ Account
│  │  │  └─ （Transactionと同様の構成）
│  │  └─ Settings
│  │     └─ （EmailForwarding / Gmail連携 / ログアウト）
│  └─ Resources
│     ├─ Assets.xcassets
│     └─ Localizable.xcstrings
└─ .gitignore
```
