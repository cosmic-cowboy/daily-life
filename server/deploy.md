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

#### Requirements

 - a free Heroku account
 - Heroku Toolbelt installed
