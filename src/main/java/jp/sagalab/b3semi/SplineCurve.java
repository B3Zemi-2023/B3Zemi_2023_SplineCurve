package jp.sagalab.b3semi;

import java.util.Arrays;

/**
 * スプライン曲線を表すクラスです。
 *
 * @author Akira Nishikawa
 */
public class SplineCurve {

  /**
   * スプライン曲線を生成します。
   *
   * @param _degree        次数
   * @param _controlPoints 制御点列
   * @param _knots         節点系列
   * @return スプライン曲線
   * @throws IllegalArgumentException スプライン曲線の次数が1未満の場合
   * @throws IllegalArgumentException 存在範囲の始点が節点系列の(次数 - 1)番目よりも小さい場合、
   *                                  または、存在範囲の終点が節点系列の(節点系列の要素数 - 次数)番目よりも大きい場合
   */
  public static SplineCurve create(int _degree, Point[] _controlPoints, Knots _knots) {
    // 次数のチェック
    if (_degree < 1) {
      throw new IllegalArgumentException("_degree < 1");
    }
    // 節点系列と制御点列の整合性チェック
    if (_knots.size() != _controlPoints.length + _degree - 1) {
      throw new IllegalArgumentException("_knots.length NOT equals (_controlPoints.length + _degree - 1).");
    }

    return new SplineCurve(_degree, _controlPoints, _knots);
  }

  /**
   * 指定されたパラメータでの点を評価します。
   *
   * @param _t パラメータ
   * @return 評価点
   */
  public Point evaluate(double _t) {
    // 対象となる節点番号を求める
    var knotNum = searchKnotNum(_t);

    // de Boor による評価
    var p = deBoor(m_degree, knotNum + 1, _t);

    return Point.createXYT(p.x(), p.y(), _t);
  }

  /**
   * de Boorアルゴリズムによって評価点を求めます。
   *
   * @param _r 次数
   * @param _i 節点区間インデックス
   * @param _t パラメーター
   * @return 評価点
   */
  public Point deBoor(int _r, int _i, double _t) {
    if (_r == 0) {
      return m_controlPoints[_i];
    }

    final double w = (m_knots.get(_i + m_degree - _r) - _t)
                   / (m_knots.get(_i + m_degree - _r) - m_knots.get(_i - 1));

    var p1 = deBoor(_r - 1, _i - 1, _t);
    var p2 = deBoor(_r - 1, _i, _t);

    return p2.internalDivision(p1, w, 1.0 - w);
  }

  /**
   * パラメーター _t における、次数 _k、制御点インデックス _j のBスプラインを求めます。
   *
   * @param _knots 節点列
   * @param _k     次数
   * @param _j     制御点インデックス
   * @param _t     パラメーター
   * @return Bスプライン
   */
  public static double bSpline(Knots _knots, int _k, int _j, double _t) {
    /* 資料 1.7節 「B スプライン表現」の式(3)を実装 */
    { // 特別な場合の処理
      int knotsSize = _knots.size();

      if (_j == 0) {
        /*### 適切な記述 ###*/
      }

      if (_j == knotsSize - _k) {
        /*### 適切な記述 ###*/
      }

      if (_k== 0) {
        /*### 適切な記述 ###*/
      }
    }

    // 通常処理
    // 分母を先に計算して0になったら（0除算が発生しそうなら）その項の係数は0とする<-これをしないと開一様に対応できない
    /*### 適切な記述 ###*/
    return 0.0;
  }

  /**
   * 節点番号の探索を行います。
   *
   * @param _t パラメーター
   * @return 節点番号
   * @throws IllegalArgumentException _tにおける節点番号が定義されないとき
   */
  public int searchKnotNum(double _t) {
    for (var i = 0; i < m_knots.size() - 1; ++i) {
      var start = m_knots.get(i);
      var end = m_knots.get(i+1);

      if (start <= _t && _t < end) {
        return i;
      }
    }

    throw new IllegalArgumentException("Knot number in _t is not defined");
  }

  /**
   * 次数を返します。
   *
   * @return 次数
   */
  public int degree() {
    return m_degree;
  }

  /**
   * 制御点列を返します。
   *
   * @return 制御点列
   */
  public Point[] controlPoints() {
    return m_controlPoints.clone();
  }

  /**
   * 節点系列を返します。
   *
   * @return 節点系列
   */
  public Knots knots() {
    return m_knots.clone();
  }

  /**
   * パラメータの範囲を返します。
   *
   * @return パラメータの範囲
   */
  public Range range() {
    return m_range.clone();
  }

  /**
   * この SplineCurve と指定された Object が等しいかどうかを比較します。
   *
   * @param obj この SplineCurve と比較される Object
   * @return 指定された Object が、このオブジェクトと
   * 次数、制御点列、節点系列、パラメータ範囲がまったく同じ SplineCurve である限りtrue
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SplineCurve other = (SplineCurve) obj;
    if (this.m_degree != other.m_degree) {
      return false;
    }
    if (!Arrays.deepEquals(this.m_controlPoints, other.m_controlPoints)) {
      return false;
    }
    if (!this.m_knots.equals(other.m_knots)) {
      return false;
    }
    return this.m_range != null && this.m_range.equals(other.m_range);
  }

  /**
   * この SplineCurve の文字列表現を返します。
   *
   * @return 次数、制御点列、節点系列、パラメータ範囲を表す String
   */
  @Override
  public String toString() {
    return String.format(
            "cp:%s\nknots:%s\ndegree:%d range:%s", Arrays.deepToString(m_controlPoints),
            m_knots.toString(), m_degree, m_range.toString());
  }

  private SplineCurve(int _degree, Point[] _controlPoints, Knots _knots) {
    m_degree = _degree;
    m_controlPoints = _controlPoints;
    m_knots = _knots;
    final var start = _knots.get(_degree - 1);
    final var end = _knots.get(_degree + 2 * _controlPoints.length - _knots.size() - 2);
    m_range = Range.create(start, end - 10e-14);
  }

  /** 次数 */
  private final int m_degree;
  /** 制御点列 */
  private final Point[] m_controlPoints;
  /** 節点系列 */
  private final Knots m_knots;
  /** パラメータ範囲 */
  private final Range m_range;
}
