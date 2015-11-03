# アプリ紹介

#### 日記一覧
![](list-ffmpeg-palette.gif)

#### 日記作成
![](create-ffmpeg-palette.gif)

#### 日記詳細
![](detail-ffmpeg-palette.gif)

#### カレンダー機能
![](calendar-ffmpeg-palette.gif)


# アプリ紹介動画の作成方法

#### 動画の作成

QuickTime Player で、Android（Genymotion）の動画を作成する 

参考
http://tokyo.secret.jp/macs/mt-quicktime-rec.html


#### GIFの作成方法


```
ffmpeg -i create.mov -vf fps=20,palettegen=stats_mode=diff -y create-palette.png
ffmpeg -i create.mov -i create-palette.png -lavfi fps=20,paletteuse -y create-ffmpeg-palette.gif
```

参考
http://genjiapp.com/blog/2015/05/05/generating-much-better-animated-gif-from-movie-file.html