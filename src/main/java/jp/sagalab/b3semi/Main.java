package jp.sagalab.b3semi;

import javax.swing.*;
import java.awt.*;
import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yako, takashima, inagaki
 */
public class Main extends JFrame {

  public Main() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setTitle("b3zemi");
    setResizable(false);

    var panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    m_canvas.setPreferredSize(new Dimension(800, 600));
    m_canvas.setBackground(Color.WHITE);
    panel.add(m_canvas);

    m_bSplineCanvas.setPreferredSize(new Dimension(800, 100));
    m_bSplineCanvas.setBackground(Color.WHITE);
    panel.add(m_bSplineCanvas);

    add(panel);

    m_canvas.addMouseListener(new MouseAdapter() {
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
    });

    pack();
    setVisible(true);
  }

  /**
   * @param _args the command line arguments
   */
  public static void main(String[] _args) {
    new Main();
  }

  public void drawSplineCurve() {
    getGraphics().setColor(Color.red);
    var controlPoints = m_controlPoints.toArray(new Point[0]);
    var knots = Knots.createUniform(3, m_controlPoints.size());
    var curve = SplineCurve.create(3, controlPoints, knots);
    var range = curve.range();

    var temp = curve.evaluate(range.start());
    var span = 0.1;

    for (var t = range.start() + span; t <= range.end(); t+=span) {
      var p = curve.evaluate(t);
      drawLine(temp, p);
      temp = p;
    }

    drawLine(temp, curve.evaluate(range.end()));

    m_bSplineCanvas.drawBSplines(knots, 3, controlPoints.length);
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

  private final BSplineCanvas m_bSplineCanvas = new BSplineCanvas();

  /** クリックで打たれた点を保持するリスト */
  private final List<Point> m_controlPoints = new ArrayList<>();

  /** 点の数の上限 */
  private static final int MAX_CONTROL_POINTS = 5;
}