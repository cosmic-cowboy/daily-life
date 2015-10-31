# Heroku への デプロイ手順

#### Gitリポジトリ内のserver以下を別リポジトリ（サブモジュール）として切り出す

```
git clone daily-life daily-server
cd daily-server
git filter-branch --subdirectory-filter server HEAD
```

#### Herokuにデプロイする

２回目以降（Herokuアプリ名を変更したくない場合）
```
git remote add heroku git@heroku.com:daily-life.git
git push -f heroku master
heroku open
heroku logs --tail
```

初回
```
heroku create
git push heroku master
heroku open
heroku logs --tail
```
#### HerokuのDB（PostgreSQL）を初期化する

DBを初期化したい場合、DBのresetコマンドを実行する。

この際、実行してよいかの確認が入るので、アプリ名を入れる。

その後、アプリを再起動する

```
heroku pg:reset DATABASE
daily-life
heroku ps:restart web.1
```


#### Requirements

 - a free Heroku account
 - Heroku Toolbelt installed
