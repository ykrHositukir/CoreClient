# CORECLIENT カスタマイズガイド

このドキュメントでは、CORECLIENTの見た目・文言・動作を変更するための編集ポイントをまとめます。
ファイルを編集したら `cargo build` で反映されます(CI経由の場合はpushするだけ)。

## 1. テーマ(配色)の変更

テーマは `packages/oneclient_app/assets/themes/*.json` のJSONで定義されています。

| ファイル | テーマ名 |
| --- | --- |
| `glass.json` | **Glass**(既定・半透明ガラス風の寒色系) |
| `image-glass.json` | **Image Glass**(写真背景+カーソルレンズ) |
| `wafu.json` | **和風 Wafu**(墨黒×朱×金) |
| `dark.json` / `light.json` / `minecraft-*.json` | 元のOneClientテーマ(互換用) |

```json
{
  "name": "My Theme",
  "radius_scale": 1.15,
  "font": "M PLUS 1p",
  "colors": {
    "light": false,
    "page": "#0F151D",
    "page_elevated": "#131B26",
    "card": "#16202D",
    "component": "#192432",
    "border": "#FFFFFF1E",
    "text": "#E9F2FC",
    "text_secondary": "#8DA0B6",
    "accent": "#4FD8EB"
  }
}
```

- `accent` がボタン・選択状態の色です。`#RRGGBB` または `#RRGGBBAA`。
- `radius_scale` を下げると角が引き締まり(和風は0.7)、上げると丸みが増します。
- 既定テーマの変更: `packages/oneclient_core/src/settings/launcher.rs` の
  `theme: "glass"` を変更、または `packages/oneclient_app/src/theme/mod.rs` の
  `BUILTIN_THEMES` の先頭を差し替えてください。

## 2. Image Glass(カーソルレンズ)の調整

実装は `packages/oneclient_app/src/layout/app_shell.rs`。

- **背景画像の差し替え**: `assets/backgrounds/image-glass-aurora.jpg` を同名で置き換えるか、
  `IMAGE_GLASS_BACKGROUND` 定数を変更。サブ画像として `image-glass-sakura.jpg` も同梱。
- **レンズ半径**: `LENS_BASE_RADIUS`(既定 170px)。呼吸アニメの幅は `sin(t * 2.0) * 9.0` の `9.0`。
- **シェーダー**: `LENS_SHADER_SOURCE`(SkSL)。屈折量は `bulge * 16.0`、色収差は `ca = bulge * 3.0`、
  リンググローは `ring` の項。拡大率は `* 0.06 * edge` の数値。
- **背景の暗さ**: `veil.set_color(Color::from_argb(188, 5, 9, 18))` のアルファ188を調整。

## 3. サイドバー / レイアウト

- 幅: `packages/oneclient_app/src/theme/mod.rs` の `SIDEBAR_WIDTH_PX`。
- 項目: `packages/oneclient_app/src/components/sidebar.rs`(アイコン・表示順・遷移先)。
- 上部バー: `packages/oneclient_app/src/components/navbar/app_navbar.rs`。

## 4. 文言(翻訳)の変更

- 英語: `packages/oneclient_app/lang/en_us.json`
- 日本語: `packages/oneclient_app/lang/ja_jp.json`(既定)

キーはそのままに値だけ書き換えてください。`%s` / `%1$s` のプレースホルダーは消さないでください。
文言を追加した場合は両方のJSONに同じキーを追加します(無い場合は英語がフォールバック表示されます)。

## 5. ロゴ・アイコン

- アプリ内ワードマーク: `packages/oneclient_app/assets/icons/icon-logo.svg`
  (SVGのグラデーション色を変えるだけでOK。テキスト部は `theme/mod.rs` の `wordmark()`)
- Windowsアイコン: `packages/oneclient_app/icons/*.png` / `icon.ico` / `icon.icns`

## 6. Modのカスタマイズ

`mods/coreessentials/` を編集し `./gradlew build`。詳細は [mods/README.md](../mods/README.md)。

## 7. 自動更新を将来有効にする(参考)

現在、CORECLIENTは更新チェックを無効化しています。有効化する場合:

1. `packages/oneclient_app/src/events.rs` の
   `crate::updater::spawn_update_check(auto_update, ...)` のコメントアウトを戻す
   (`auto_update` 変数の復元も必要)。
2. `packages/oneclient_app/src/constants.rs` の `UPDATER_ENDPOINT` を
   `https://github.com/<あなたの組織>/<リポジトリ>/releases/latest/download/latest.json` に変更。
3. cargo-packager に minisign秘密鍵を渡して署名(`CARGO_PACKAGER_SIGN_PRIVATE_KEY`)し、
   公開鍵を `UPDATER_PUBKEY` に設定。
4. Releaseに `latest.json`(updater manifest)を添付するワークフローを追加。

※旧Polyfrostの公開鍵のままでは検証が失敗するため、必ず自分の鍵ペアを生成してください。

## 8. ソーシャル(フレンド/パーティ)を再有効化する(参考)

`packages/oneclient_app/src/chat.rs` の `chat_allowed()` が常時 `false` を返しています。
コメントで元のロジックを残してあるので、そちらに戻すと フレンド/チャット/ロビーUI が復活します。
