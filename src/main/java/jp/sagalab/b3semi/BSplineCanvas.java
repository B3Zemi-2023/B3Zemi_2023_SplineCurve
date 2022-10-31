package jp.sagalab.b3semi;

import java.awt.*;
import java.awt.geom.Line2D;

public class BSplineCanvas extends Canvas {
  /**
   * このキャンバス上にBスプラインを描画する（定義域の線もこのメソッドで描画）
   *
   * @param _knots             節点列
   * @param _degree            次数
   * @param _controlPointsSize 制御点
   */
  public void drawBSplines(Knots _knots, int _degree, int _controlPointsSize) {
    // 定義域の線を描画
    drawDomainLines(_knots, _degree, _controlPointsSize);

    // Bスプラインを描画
    /*### 適切な記述 ###*/
    /* SplineCurveクラスの静的メソッドbSpline()を使って上手くやる */
    /* 注) Bスプラインを求めるパラメーターtとしてrange.end()を入れる際、
    　　　　range.end()そのものは定義域外で評価できないため、range.end()と等値にならない程度に減らすといい（10e-14とか） */
  }

  /**
   * Bスプラインの値を、このキャンバス内で表される点に変換する
   *
   * @param _knotsRange 節点列全体の範囲
   * @param _bSpline    Bスプライン
   * @param _t          パラメーター
   * @return キャンバス内で表される点
   */
  private Point bSplineToPoint(Range _knotsRange, double _bSpline, double _t) {
    final var width = getWidth();
    final var height = getHeight();
    final var rangeLength = _knotsRange.length();

    return Point.createXY(width * _t / rangeLength, height * (1 - _bSpline));
  }

  /**
   * キャンバス上に_p1と_p2を結ぶ線を描画する
   *
   * @param _p1    点1
   * @param _p2    点2
   * @param _color 色
   */
  private void drawLine(Point _p1, Point _p2, Color _color) {
    Graphics2D g = (Graphics2D)getGraphics();
    g.setColor(_color);
    g.setStroke(new BasicStroke(2));

    Line2D.Double line = new Line2D.Double(_p1.x(), _p1.y(), _p2.x(), _p2.y());
    g.draw(line);
  }

  /**
   * 定義域の縦線を描画する
   *
   * @param _knots             節点列
   * @param _degree            次数
   * @param _controlPointsSize 制御点数
   */
  private void drawDomainLines(Knots _knots, int _degree, int _controlPointsSize) {
    final var width = getWidth();
    final var height = getHeight();
    final var rangeLength = _knots.range().length();

    { // 定義域始点の縦線を描画
      final var value = /*### 適切な記述 ###*/0.0;
      final var x = width * value / rangeLength;
      drawLine(Point.createXY(x, 0), Point.createXY(x, height), Color.RED);
    }

    { // 定義域終点の縦線を描画
      final var value = /*### 適切な記述 ###*/0.0;
      final var x = width * value / rangeLength;
      drawLine(Point.createXY(x, 0), Point.createXY(x, height), Color.RED);
    }
  }
}
