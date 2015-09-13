# daily-life
日々のできごと、見たこと、感じたこと、印象に残ったことを自分のために書く日記アプリ

### 各APIについて

#### 日記取得

| HTTPメソッド | URI |
|:-----------|:------------:|
| GET       | (ホスト名)/user/api/v1/entry|

例）

`curl -v localhost:9000/user/api/v1/entry -X GET -H "Content-Type: application/json"`

#### 日記の文字データの登録

| HTTPメソッド | URI |
|:-----------|:------------:|
| POST       | (ホスト名)/user/api/v1/entry|

例）

`curl -v localhost:9000/user/api/v1/entry -X POST -H "Content-Type: application/json" -d "{\"content\":\"日記内容\"}"`

#### 日記の画像データの登録

| HTTPメソッド | URI |
|:-----------|:------------:|
| POST       | (ホスト名)/user/api/v1/entry/image|

例）

`curl -v localhost:9000/user/api/v1/entry/image -X POST -H "Content-Type: application/json" -d @画像のパス`

#### 画像データの取得

| HTTPメソッド | URI | 
|:-----------|:------------:|
| POST       | (ホスト名)/user/api/v1/entry/file/image|

リクエストパラメータ

| パラメータ名 | 指定する値 | 必須 | 説明 |
|:-----------|:--------:|:---:|:----:|
| fileId     |ファイルID|必須|ファイルのIDを指定します|

例）

`curl -v localhost:9000/user/api/v1/file/image?fileId=ファイルID -X GET -H "Content-Type: application/json"`

### リンク

[計画書](PLAN.md)
