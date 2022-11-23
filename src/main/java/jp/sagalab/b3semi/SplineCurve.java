package jp.sagalab.b3semi;

import java.util.ArrayList;
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
    if (_knots.length() != _controlPoints.length + _degree - 1) {
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
    var knotNum = knotIntervalIndex(_t);

    // de Boor による評価
    var p = deBoor(m_degree, knotNum + 1, _t);

    return Point.createXYT(p.x(), p.y(), _t);
  }

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

  public static double bSpline(Knots _knots, int _k, int _j, double _t) {
    { // 特別な場合の処理
      int knotsSize = _knots.length();

      // 左端ではブレンドの左側を考慮しない（p.48 図3.14、p.50 図3.16、図3.17）
      if (_j == 0) {
        double coeff = (_knots.get(_j + _k) - _t) / (_knots.get(_j + _k) - _knots.get(_j));
        return coeff * bSpline(_knots, _k- 1, _j + 1, _t);
      }

      // 右端ではブレンドの右側を考慮しない（p.48 図3.14、p.50 図3.16、図3.17）
      if (_j == knotsSize - _k) {
        double coeff = (_t - _knots.get(_j - 1)) / (_knots.get(_j + _k- 1) - _knots.get(_j - 1));
        return coeff * bSpline(_knots, _k- 1, _j, _t);
      }

      if (_k== 0) {
        return (_knots.get(_j - 1) <= _t && _t < _knots.get(_j)) ? 1.0 : 0.0;
      }
    }

    // 通常処理
    // 分母を先に計算して0になったら（0除算が発生しそうなら）その項の係数は0とする
    double denom1 = _knots.get(_j + _k- 1) - _knots.get(_j - 1);
    double denom2 = _knots.get(_j + _k) - _knots.get(_j);
    double coeff1 = (denom1 != 0.0) ? (_t - _knots.get(_j - 1)) / denom1 : 0.0;
    double coeff2 = (denom2 != 0.0) ? (_knots.get(_j + _k) - _t) / denom2 : 0.0;

    return coeff1 * bSpline(_knots, _k- 1, _j, _t) + coeff2 * bSpline(_knots, _k- 1, _j + 1, _t);
  }

  /**
   * 特定のパラメータにおける節点範囲のインデックスを返します
   *
   * @param _t        パラメータ
   * @return 節点番号
   * @throws IllegalArgumentException _tにおける節点番号が定義されないとき
   */
  public int knotIntervalIndex(double _t) {
    for (var i = 0; i < m_knots.length() - 1; ++i) {
      var start = m_knots.get(i);
      var end = m_knots.get(i+1);

      if (start <= _t && _t < end) {
        return i;
      }
    }

    throw new IllegalArgumentException("Knot number in _t is not defined");
  }

  /**
   * 節点を挿入し、挿入後の新たなスプライン曲線を返します
   *
   * @param _knot        挿入する節点
   * @return 節点挿入後のスプライン曲線
   * @throws IllegalArgumentException _knotが定義域内に含まれないとき
   */
  public SplineCurve knotInserted(double _knot) {
    /** 適切な記述 **/
    return null;
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
  public Range domain() {
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
    final var end = _knots.get(_degree + 2 * _controlPoints.length - _knots.length() - 2);
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
