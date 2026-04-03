# JPQuiz

旅行シーンを中心に、日本語の単語や文章をクイズ形式で学習できるWebサービスです。  
ユーザーはシーン別クイズを解き、間違いノートやお気に入り機能を通じて反復学習ができます。  
また、管理者は通報処理や統計画面を通じてサービスデータを管理できます。

## プロジェクト概要

- プロジェクト名: JPQuiz
- 一言紹介: 旅行日本語学習のためのシーンベースクイズサービス
- 開発目的:
  - 単純な暗記型学習ではなく、実際の旅行シーンに近い問題解決体験を提供する
  - ユーザーの学習履歴をもとに、間違い復習、お気に入り、統計機能を提供する
  - Spring Boot + Vueベースの実践的なフルスタック開発経験を積む

## 主な機能

- 会員機能
  - 会員登録、ログイン、JWT（JSON Web Token、トークンベース認証）認証
  - マイページ情報照会、会員情報修正、退会
- クイズ機能
  - 単語／文章タイプ別クイズ開始
  - シーン（scene）ベースの問題出題
  - 問題照会、解答提出、採点、結果照会
  - 非会員のクイズ利用対応
- 学習補助機能
  - 間違いノートの自動保存および復習セット生成
  - お気に入り登録／解除および一覧照会
- 通報／運営機能
  - 問題エラー通報登録
  - 管理者向け通報一覧照会および状態変更
- 統計機能
  - ユーザー学習統計照会
  - 管理者向け全体統計／設問統計照会

## 技術スタック

### Backend
- Java 17
- Spring Boot 3.5.10
- Spring Security
- JPA（Java Persistence API）
- MyBatis
- JWT（JSON Web Token、トークンベース認証）

### Frontend
- Vue 3
- Vite
- Pinia
- Axios

### Database / Infra
- MariaDB
- Docker
- Jenkins

## アーキテクチャ

このプロジェクトは、機能中心のパッケージ構成とレイヤー分離をベースに構成されています。

- ドメイン別構成
  - `member`
  - `quiz`
  - `report`
  - `stats`
- レイヤー分離
  - `presentation`: Controller（APIエントリーポイント）
  - `application`: Service（ビジネスロジック）
  - `domain`: Entity / Enum / ドメインモデル
  - `infrastructure`: Repository / Mapper / 外部連携

また、読み取りと書き込みの責任を分離するCQRS（Command Query Responsibility Segregation）の方向性を採用しています。

- Member: JPA中心
- Quiz / Stats / Report: MyBatisの活用比重が高い

## 認証 / 例外処理

- 認証
  - Spring Security + JWTベースのステートレス（StateLess、サーバーセッション未使用）認証
  - Access Token + Refresh Token再発行構造を適用
- 例外処理
  - `CustomException(ErrorCode)` + `GlobalExceptionHandler` パターンを使用
  - 401 / 403 も共通エラーフォーマットで応答
- レスポンス形式
  - 成功: `ApiResponse<T>`
  - 失敗: `ErrorResponse`

## 実行方法

### 1. バックエンド実行

事前準備
- Java 17
- MariaDB

設定ファイル準備
1. `src/main/resources/application-local.yml.example` ファイルをコピーします。
2. `application-local.yml` ファイルを作成します。
3. ローカルDBおよびJWT設定値を入力します。
