package jp.sagalab.b3semi;

import javax.swing.*;
import java.awt.*;
import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yako, takashima, inagaki
 */
public class Main extends JFrame {

  public Main() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    m_canvas.setSize(800, 600);
    m_canvas.setBackground(Color.WHITE);
    setTitle("b3zemi");
    add(m_canvas);
    pack();
    setVisible(true);

    m_canvas.addMouseListener(
            new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent e) {
                if (m_controlPoints.size() < MAX_CONTROL_POINTS) {
                  Point p = Point.createXY(e.getX(), e.getY());

                  // 打った点をリストに追加する
                  m_controlPoints.add(p);
                  // 打った点を描画する
                  drawPoint(p.x(), p.y());

                  // 点が揃ったら
                  if (m_controlPoints.size() == MAX_CONTROL_POINTS) {
                    // スプライン曲線の生成、描画を行う
                    drawSplineCurve();
                  }
                }
              }
            }
    );
  }

  /**
   * @param _args the command line arguments
   */
  public static void main(String[] _args) {
    new Main();
  }

  public void drawSplineCurve() {
    /* ↓ここから必要な処理を書き足していく↓ */
    // コツ: SplineCurve.create(args ・・・)でインスタンス生成を行い(引数は自分で考える)、
    // SplineCurve.evaluate(_t) と drawLine(_p1, _p2) を駆使する

    //リストを配列に変換する
    Point[] cpPoints = m_controlPoints.toArray(new Point[0]);
    Range range = Range.create(0.0, 1.0);
    int degree = 3;
    //double[] knots = createKnots(degree, range, cpPoints.length);//
    double[] knots = new double[]{0.0, 0.0, 0.0, 0.1, 0.95, 0.99, 1.0, 1.0, 1.0};
    System.out.println(Arrays.toString(knots));
    SplineCurve spline = SplineCurve.create(degree, cpPoints, knots, range);
    List<Point> evaluatePoints = new ArrayList<>();
    for (double n = 0 ; n <= 1 ; n += 0.001) {
      Point i = spline.evaluate(n);
      evaluatePoints.add(i);
    }
    for (int n = 0 ; n < evaluatePoints.size() - 1 ; n ++) {
      drawLine(evaluatePoints.get(n), evaluatePoints.get(n+1));
    }
  }

  /**
   * 次数と制御点数から節点の区間数を求め、間を等分するような節点系列を返す。
   *
   * @param _degree   次数
   * @param _range    存在範囲
   * @param _cpLength 制御点数
   * @return 節点系列
   */
  private static double[] createKnots(int _degree, Range _range, int _cpLength) {
    // 節点系列の生成
    double start = _range.start();
    double end = _range.end();
    // 有効定義域の節点区間数
    int knotIntervalNum = _cpLength - _degree;
    double[] knots = new double[knotIntervalNum + 2 * _degree - 1];

    for (int i = 0; i < knots.length; ++i) {
      double w = (i - _degree + 1) / (double) knotIntervalNum;
      knots[i] = (1.0 - w) * start + w * end;
    }
    return knots;
  }

  /**
   * 点を描画する
   *
   * @param _x x座標
   * @param _y y座標
   */
  public void drawPoint(double _x, double _y) {
    Graphics2D g = (Graphics2D) m_canvas.getGraphics();
    double radius = 3;

    Ellipse2D.Double oval = new Ellipse2D.Double(_x - radius, _y - radius, radius * 2, radius * 2);
    g.draw(oval);
  }

  /**
   * 線を描画する
   * @param _p1 始点
   * @param _p2 終点
   */
  public void drawLine(Point _p1, Point _p2) {
    Graphics2D g = (Graphics2D)m_canvas.getGraphics();

    Line2D.Double line = new Line2D.Double(_p1.x(), _p1.y(), _p2.x(), _p2.y());
    g.draw(line);
  }

  /** キャンバスを表す変数 */
  private final Canvas m_canvas = new Canvas();

  /** クリックで打たれた点を保持するリスト */
  private final List<Point> m_controlPoints = new ArrayList<>();

  /** 点の数の上限 */
  private static final int MAX_CONTROL_POINTS = 7;
}