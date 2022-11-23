package jp.sagalab.b3semi;

import javax.swing.*;
import java.awt.*;
import java.awt.Canvas;
import java.awt.event.*;
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
    setTitle("b3semi");
    setResizable(false);

    var panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    var textField = new JTextField();
    var button = new JButton("insert");
    textField.setMaximumSize(new Dimension(200, 40));
    panel.add(textField);
    panel.add(button);

    button.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        final var knot = Double.parseDouble(textField.getText());

        m_curve = m_curve.knotInserted(knot);

        m_canvas.getGraphics().clearRect(0, 0, 800, 600);
        drawSplineCurve(m_curve);

        for (var cp : m_curve.controlPoints()) {
          drawPoint(cp.x(), cp.y());
        }

        System.out.println(Arrays.toString(m_curve.knots().asArray()));
      }
    });

    m_canvas.setPreferredSize(new Dimension(800, 600));
    m_canvas.setBackground(Color.WHITE);
    panel.add(m_canvas);

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
            var controlPoints = m_controlPoints.toArray(new Point[0]);
            var knots = Knots.createUniform(3, m_controlPoints.size());

            m_curve = SplineCurve.create(3, controlPoints, knots);

            // スプライン曲線の生成、描画を行う
            drawSplineCurve(m_curve);
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

  public void drawSplineCurve(SplineCurve _curve) {
    getGraphics().setColor(Color.red);
    var domain = _curve.domain();

    var temp = _curve.evaluate(domain.start());
    var span = 0.01;

    for (var t = domain.start() + span; t <= domain.end(); t+=span) {
      var p = _curve.evaluate(t);
      drawLine(temp, p);
      temp = p;
    }

    drawLine(temp, _curve.evaluate(domain.end()));
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

  private SplineCurve m_curve;

  /** 点の数の上限 */
  private static final int MAX_CONTROL_POINTS = 5;
}