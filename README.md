<div align="center">

# CORECLIENT

ガラスをまとった、次世代のMinecraftランチャー。
**Polyfrost OneClient (OneLauncher)** をベースに、CORECLIENT Projectが再設計しました。

</div>

## 特徴

- **Glass / Image Glass / 和風 テーマ** — 既定は半透明ガラスの「Glass」。**Image Glass** では背景画像がマウスカーソルの近くだけレンズのように鮮明に浮かび上がります(屈折・色収差・リンググロー付きのカスタムSkiaシェーダー)。
- **左サイドバーナビゲーション** — ホーム / バージョン / ブラウズ / ワードローブ / 設定。
- **日本語・英語切替** — 既定は日本語。設定からいつでも変更できます。
- **ワンクリックMod導入** — OneConfigバンドルシステムをそのまま搭載。CurseForge / Modrinthのブラウズにも対応。
- **独自Mod同梱** — [`mods/`](./mods/README.md) にCORECLIENT Essentials (Fabric 1.21.11) のソース。FPS・座標・キーストロークHUD。
- **Microsoft正規認証** — EULAに準拠した正規アカウントログイン。
- **パーティ・フレンド機能は非搭載** — ソーシャル機能は本フォークでは無効化しています(将来再有効化も可能)。
- **自動更新は現在オフ** — 有効化手順は [docs/CUSTOMIZE.md](./docs/CUSTOMIZE.md) を参照。

## 配布物のダウンロード

1. GitHubの **Actions** タブ → `CORECLIENT Release Build` → 最新のrun
2. `CORECLIENT-windows-x64` をダウンロード
3. 中身は2つ:
   - `CORECLIENT-portable.zip` — 解凍して `CORECLIENT.exe` を実行(ポータブル)
   - `CORECLIENT-setup.exe` — NSISインストーラー

タグ(`v1.0.0` 等)をpushすると同じ内容が **Releases** に自動公開されます。
※ビルドは無署名のため、初回起動時にWindows SmartScreenの警告が出ることがあります(「実行」で続行)。

## ソースからのビルド(Windows)

必要環境: **Rust (stable, 1.97以降)** ・ Git ・ Python 3(VC++ランタイム同梱スクリプト用)

```bash
git clone https://github.com/CORECLIENT/CoreClient.git
cd CoreClient
python distribution/windows/bundle-crt.py
cargo install cargo-packager --locked --version 0.11.8
cargo build --release
cargo packager --release --formats nsis
# 成果物: target/release/oneclient_app.exe (ポータルEXE)
#         target/release/*-setup.exe   (インストーラー)
```

## リポジトリ構成

| パス | 内容 |
| --- | --- |
| `packages/oneclient_app` | ランチャー本体 (Rust + Freya 0.5)。UI・テーマ・i18n |
| `packages/oneclient_core` ほか | ゲーム起動・クラスタ管理・Java管理などのコア |
| `packages/oneclient_app/assets/themes` | **テーマ定義JSON** (`glass.json` / `image-glass.json` / `wafu.json` ほか) |
| `packages/oneclient_app/lang` | **翻訳ファイル** (`en_us.json` / `ja_jp.json`) |
| `mods/coreessentials` | 独自Mod (Fabric) のソースとビルド |
| `docs/CUSTOMIZE.md` | デザイン・文言・レンズ効果のカスタマイズガイド |

## ライセンス

このリポジトリは元プロジェクトのライセンスを継承し **GPL-3.0-only** で提供します。
原著作者(Polyfrost)のクレジットは [ATTRIBUTION.md](./ATTRIBUTION.md) に記載しています。
改変版を配布する場合も同じくGPL-3.0でソースを公開してください。
