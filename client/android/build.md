# リリース用APKの作成

### 事前準備

リリース用のAPKを作成する場合、下記のファイルを指定のディレクトリに配置してください。
 - keystoreファイル
 - storePassword, keyPasswordを記載したプロパティファイル

※ 現在はローカル環境でAPKの作成を行っているため、HOMEディレクトリ配下にkeysディレクトリを作成し、下記のファイルを配置してください。

### リリース用APKの作成

gradlewがある本ディレクトリにて実行する
```
./gradlew clean assembleRelease
```

### 参考

[Android Studio(Gradle)でapkファイルを作成する](http://qiita.com/shiraji/items/8f55b5295094487ce71a)
