# B3Zemi_2023_SplineCurve
Canvasに制御点を打って、その制御点をもとにSpline曲線の描画を行うプログラムです。

## 作業手順
1. このリポジトリをローカルにcloneする.
2. cloneしてきたmaster-branchから新たに自分のブランチを作成する.
3. 新たに作成した自分のブランチに切り替える.
4. MainクラスのdrawSplineCurveメソッドに処理を書き込んで,Spline曲線が描画できるように改良する.
5. GitHubに自分のブランチをpushする.(add, commitを忘れずに!)
6. 次数やknot,制御点数などを変えてみて,Spline曲線がどう変化するのか試してみる.

## 処理の流れ(ヒント)
1. 制御点をMAX_CONTROL_POINTSの数だけ入力する.
2. drawSplineCurveメソッドが呼び出される.
3. SplineCurveクラスにあるcreateを用いてSpline曲線のインスタンス生成を行う.
4. evaluateメソッドを用いて評価点を取得する.
5. 得られた評価点からSpline曲線の描画を行う.
