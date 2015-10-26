# デモデータの作成方法

#### 日記を投稿する

画像を含める場合は自前に画像を投稿し、ファイルIDを取得する
```
curl -v https://daily-life.herokuapp.com/user/api/v1/entry/image -X POST -H "Content-Type: application/json" -d @画像のパス
```

画像を含める場合は、fileIdを追加し、日記を投稿する
```
curl -v https://daily-life.herokuapp.com/user/api/v1/entry -X POST -H "Content-Type: application/json" -d "{\"content\":\"日記内容\",\"postDate\":\"YYYYMMDD\",\"fileId\":\"00000\"}"
```
