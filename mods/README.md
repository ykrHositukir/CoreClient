# CORECLIENT オリジナルMod (Fabric)

CORECLIENTに同梱する独自Modのソースコードです。

## 収録Mod

| Mod | ID | 内容 |
| --- | --- | --- |
| CORECLIENT Essentials | `coreessentials` | FPS / 座標 / 方角 / ワールド時計 / プレイ時間のHUD、WASD・マウスのキーストローク表示、F4でHUD切替、F6でキーストローク切替 |

対象: **Minecraft 1.21.11 (Fabric / Fabric API 0.141.6)** ・Java 21

ライセンスはランチャーと同じ **GPL-3.0** です(元のOneClientがGPL-3.0のため)。

## ローカルビルド

```bash
cd mods/coreessentials
./gradlew build          # Windows は gradlew.bat build
# -> build/libs/coreessentials-1.0.0.jar
```

生成されたjarは、ランチャー側の「バージョン → Mod」画面から導入するか、
`.minecraft/mods` に直接置けます。

## GitHub Actionsでのビルド

`main` ブランチに push すると `.github/workflows/coreclient_mods.yml` が自動で
ビルドし、jarをActionsの成果物(Arifacts)に添付します。タグ(`v1.0.0` など)を
打つとランチャーのReleaseにMod jarも同時に添付されます。

## カスタマイズのヒント

- HUDの位置: ゲーム内で `config/coreessentials.json` の `hudX` / `hudY` を編集
- 追記文言: `src/main/resources/assets/coreessentials/lang/ja_jp.json`
- 色の変更: `hud/HudOverlay.java` 上部の `PANEL_BG` などの定数
- Minecraft/Fabricバージョン更新: `gradle.properties` の4つのバージョン
