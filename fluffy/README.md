# nekodion-db

## 開発コマンド（ローカル）

```sh
# 移動
cd ../scratch/local

# DB起動
docker compose up -d db

# Flywayの状況確認
docker compose run --rm flyway info

# マイグレーションの実行
docker compose run --rm flyway migrate

# マイグレーションの実行（エラー時など、強制的に実行）
docker compose run --rm flyway repair
docker compose run --rm flyway migrate

# DBクリーン（スキーマ内のオブジェクトをすべて削除）
# ※ Flyway 10以降はcleanがデフォルト無効。FLYWAY_CLEAN_DISABLED=false で明示的に許可する
docker compose run --rm -e FLYWAY_CLEAN_DISABLED=false flyway clean
# または Docker ボリュームごと削除する場合
docker compose down -v

# ログイン
docker exec -it nekodion-db mysql -u user -ppass
```
