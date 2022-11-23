# B3Zemi_2022_SplineCurve

Canvasに制御点を打って、その制御点をもとにSpline曲線の描画を行うプログラムです。

```
$ git clone git@github.com:B3Zemi-2022/B3Zemi_2022_SplineCurve.git
$ git checkout -b challenge2/(yourName)
$ git fetch origin challenge2
$ git reset --hard origin/challenge2

# 作業が終わったらコミットしてプッシュ
$ git push -u origin challenge2/(yourName)
```

## 課題概要

Bスプライン曲線には、節点挿入アルゴリズムというものがあり、曲線の形を変えずに節点を挿入することができます。
曲線の形が変わらないのは、挿入した節点によって制御点の位置を再構成するためです。

### 節点挿入アルゴリズム

挿入する節点を $x$ とし、節点挿入後の節点列を $u$ とします。
ここで、 $x$ は曲線の定義域内の値である必要があり、節点挿入後の節点列は昇順でなければなりません。
$x$ を適切な位置に挿入した後、再構成される制御点は以下のようになります。なお、 $d$ を再構成前の制御点、 $\hat{d}$ を再構成後の制御点とします。また、 $n$ は曲線の次数、 $I$ は $x$ が属する節点区間のインデックスを表しています。

```math
\hat{d}_{i} = \left\{
  \begin{align}   
    & d_i & (i = 0,\dots,I-n+1) \\   
    & \frac{u_{I+1} - u_{i-1}}{u_{i+n} - u_{i-1}}d_i + \frac{u_{i+n} - u_{I+1}}{u_{i+n} - u_{i-1}}d_{i-1} & (i = I-n+2,\dots,I+1) \\
    & d_{i-1} & other
  \end{align} 
  \right. 
```

## 実装してほしい部分

1. SplineCurveクラスの`knotInserted()`