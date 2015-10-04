# daily-life
日々のできごと、見たこと、感じたこと、印象に残ったことを自分のために書く日記アプリ

### 各APIについて

#### 日記取得

| HTTPメソッド | URI |
|:-----------|:------------:|
| GET       | (ホスト名)/user/api/v1/entry|

例）

`curl -v localhost:9000/user/api/v1/entry -X GET -H "Content-Type: application/json"`

#### 指定した日記取得

| HTTPメソッド | URI |
|:-----------|:------------:|
| GET       | (ホスト名)/user/api/v1/entry|

リクエストデータ

| パラメータ名 | 指定する値 | 必須 | 説明 |
|:-----------|:--------:|:---:|:----:|
| entryId     |日記エントリID|必須|日記エントリのIDを指定します|

例）

`curl -v localhost:9000/user/api/v1/entry?entryId=日記エントリID -X GET -H "Content-Type: application/json"`


#### 日記投稿

| HTTPメソッド | URI |
|:-----------|:------------:|
| POST       | (ホスト名)/user/api/v1/entry|

リクエストデータ

| パラメータ名 | 指定する値 | 必須 | 説明 |
|:-----------|:--------:|:---:|:----:|
| content    |投稿内容|必須|投稿内容を指定します|
| fileId     |ファイルID|オプション|ファイルのIDを指定します|

レスポンスデータ

| パラメータ名 | 指定する値 | 説明 |
|:-----------|:--------:|:----:|
| entryId    |日記エントリID|日記エントリのIDを指定します|

例）

`curl -v localhost:9000/user/api/v1/entry -X POST -H "Content-Type: application/json" -d "{\"content\":\"日記内容\",\"fileId\":\"00000\"}"`

#### 画像登録

| HTTPメソッド | URI |
|:-----------|:------------:|
| POST       | (ホスト名)/user/api/v1/entry/image|

リクエストデータ：InputStream

レスポンスデータ

| パラメータ名 | 指定する値 | 説明 |
|:-----------|:--------:|:----:|
| fileId     |ファイルID|ファイルのIDを指定します|

例）

`curl -v localhost:9000/user/api/v1/entry/image -X POST -H "Content-Type: application/json" -d @画像のパス`

#### 画像データの取得

| HTTPメソッド | URI | 
|:-----------|:------------:|
| GET       | (ホスト名)/user/api/v1/entry/file/image|

リクエストパラメータ

| パラメータ名 | 指定する値 | 必須 | 説明 |
|:-----------|:--------:|:---:|:----:|
| fileId     |ファイルID|必須|ファイルのIDを指定します|

例）

`curl -v localhost:9000/user/api/v1/file/image?fileId=ファイルID -X GET -H "Content-Type: application/json"`

#### 指定した日記の削除

| HTTPメソッド | URI |
|:-----------|:------------:|
| GET       | (ホスト名)/user/api/v1/entry|

リクエストデータ

| パラメータ名 | 指定する値 | 必須 | 説明 |
|:-----------|:--------:|:---:|:----:|
| entryId    |日記エントリID|必須|日記エントリのIDを指定します|

例）

`curl -v localhost:9000/user/api/v1/entry?entryId=日記エントリID -X DELETE -H "Content-Type: application/json"`


### DBの確認方法

`http://localhost:9000/console/`

にアクセスし、driverClassName, url, username, passwordを入力

[計画書](PLAN.md)
