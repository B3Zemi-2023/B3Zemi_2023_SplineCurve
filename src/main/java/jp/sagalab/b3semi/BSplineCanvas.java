package jp.sagalab.b3semi;

import java.awt.*;
import java.awt.geom.Line2D;

public class BSplineCanvas extends Canvas {
  public void drawBSplines(Knots _knots, int _degree, int _controlPointsSize) {
    final var range = _knots.range();
    final var span = 0.1;

    drawDomainLines(_knots, _degree, _controlPointsSize);

    for (var i = 0; i < _controlPointsSize; ++i) {
      var temp = SplineCurve.bSpline(_knots, _degree, i, 0.0);

      for (var t = range.start() + span; t <= range.end(); t+=span) {
        final var bSpline = SplineCurve.bSpline(_knots, _degree, i, t);

        drawLine(bSplineToPoint(range, temp, t - span), bSplineToPoint(range, bSpline, t), Color.BLACK);
        temp = bSpline;
      }

      final var last = SplineCurve.bSpline(_knots, _degree, i, range.end() - 10e-14);
      drawLine(bSplineToPoint(range, temp, range.end() - span), bSplineToPoint(range, last, range.end()), Color.BLACK);
    }
  }

  private Point bSplineToPoint(Range _knotsRange, double _bSpline, double _t) {
    final var width = getWidth();
    final var height = getHeight();
    final var rangeLength = _knotsRange.length();

    return Point.createXY(width * _t / rangeLength, height * (1 - _bSpline));
  }

  private void drawLine(Point _p1, Point _p2, Color _color) {
    Graphics2D g = (Graphics2D)getGraphics();
    g.setColor(_color);
    g.setStroke(new BasicStroke(2));

    Line2D.Double line = new Line2D.Double(_p1.x(), _p1.y(), _p2.x(), _p2.y());
    g.draw(line);
  }

  private void drawDomainLines(Knots _knots, int _degree, int _controlPointsSize) {
    final var width = getWidth();
    final var height = getHeight();
    final var rangeLength = _knots.range().length();
    {
      final var value = _knots.get(_degree - 1);
      final var x = width * value / rangeLength;
      drawLine(Point.createXY(x, 0), Point.createXY(x, height), Color.RED);
    }
    {
      final var value = _knots.get(_controlPointsSize - 1);
      final var x = width * value / rangeLength;
      drawLine(Point.createXY(x, 0), Point.createXY(x, height), Color.RED);
    }
  }
}
