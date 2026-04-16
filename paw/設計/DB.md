# データベース設計

- flywayで管理する
- 日本時刻
- UTF-8
- flywayの実行はdockerで行う

```
fluffy
├─ src/main/resources/db/migration
│  ├─ V0_1_0_1__xxx.sql
│  ├─ V0_1_0_2__xxx.sql
│  └─ V0_2_0_1__xxx.sql
├─ build.gradle
├─ Dockerfile
└─ settings.gradle
```
