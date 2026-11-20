# CORECLIENT はじめてのリリース手順

このリポジトリをあなたのGitHubにpushするだけで、Windows用EXEと独自Modが自動ビルドされます。

## 1. リポジトリを作成してpushする

1. GitHubで**public**リポジトリを作成(例: `yourname/CoreClient`)。
2. このフォルダの中身をそのままpushします。

```bash
git init
git add .
git commit -m "CORECLIENT initial import (based on Polyfrost OneClient)"
git branch -M main
git remote add origin https://github.com/yourname/CoreClient.git
git push -u origin main
```

3. push後、**Actions**タブで2つのワークフローが走ります。
   - `CORECLIENT Release Build` → ポータブルZIP + NSISインストーラー
   - `CORECLIENT Mods Build` → CORECLIENT Essentialsのjar

   初回はキャッシュがないため30〜50分ほどかかります(以降は速くなります)。

## 2. 成果物を取る

- **Actions** → 完了したrun → `CORECLIENT-windows-x64` と `coreclient-mods` をダウンロード。
- `CORECLIENT-portable.zip` を解凍 → `CORECLIENT.exe` を起動。
  (同梱DLLは解凍したフォルダにexeと並べて置いてください)

## 3. 正式リリースとして公開する

タグをpushするだけでReleasesに自動公開されます。

```bash
git tag v1.0.0
git push origin v1.0.0
```

→ `Releases` に `CORECLIENT-portable.zip` / `CORECLIENT-setup.exe` / Mod jar が添付されたリリースが作られます。

## 4. よくある質問

**Q. 起動時に青い画面(SmartScreen)が出る**
無署名ビルドのためです。「詳細情報」→「実行」で起動できます。署名証明書を取得すれば解消します。

**Q. ビルドが失敗する**
Actionsのログを確認してください。Rustのバージョンは `stable`(1.97以降)が必要です。

**Q. 自動更新は?**
現在オフです。有効化手順は `docs/CUSTOMIZE.md` の §7 を参照してください。

**Q. 色や文言を変えたい**
`docs/CUSTOMIZE.md` にすべての編集ポイントをまとめています。

## 5. ライセンス上の注意(必読)

本リポジトリは Polyfrost **OneClient/OneLauncher** (GPL-3.0) の改変版です。
GPL-3.0により、**あなたが配布するビルドにもソースコードの公開義務があります**。
publicリポジトリで配布していればこの義務を満たします。原著作者の表記
(`ATTRIBUTION.md`)は削除しないでください。また、あなたの改変を示す
文言への書き換えは自由ですが、Original work by Polyfrost の表示は保持してください。
