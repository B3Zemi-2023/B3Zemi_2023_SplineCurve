package jp.sagalab.b3semi;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Knots implements Cloneable {
  public static Knots create(double... _initialValues) {
    for (var i = 0; i < _initialValues.length; ++i) {
      final var value = _initialValues[i];
      // 値のバリデート
      validateValue(value);

      if (i == 0) continue;

      final var preValue = _initialValues[i-1]; // 一個前の値
      // 節点列は昇順でなければならない
      if (value < preValue) {
        throw new IllegalArgumentException("Knots must be ascending order.");
      }
    }

    return new Knots(_initialValues);
  }

  /**
   * 一様の節点列を生成する
   *
   * @param _degree
   * @param _cpLength
   * @return
   */
  public static Knots createUniform(int _degree, int _cpLength) {
    var knots = new double[_cpLength + _degree - 1];

    for (var i = 0; i < knots.length; ++i) {
      knots[i] = i;
    }

    return create(knots);
  }

  /**
   * 開一様の節点列を生成する
   *
   * @param _degree
   * @param _cpLength
   * @return
   */
  public static Knots createOpenUniform(int _degree, int _cpLength) {
    var knots = new double[_cpLength + _degree - 1];

    for (var i = 0; i < _degree; ++i) {
      knots[i] = 0;
    }
    for (var i = _degree; i < knots.length - _degree; ++i) {
      knots[i] = i - _degree + 1;
    }
    for (var i = knots.length - _degree; i < knots.length; ++i) {
      knots[i] = knots[knots.length - _degree - 1] + 1;
    }

    return create(knots);
  }

  public double get(int _index) {
    return m_knotsArray[_index];
  }

  public int size() {
    return m_knotsArray.length;
  }

  public Range range() {
    return Range.create(m_knotsArray[0], m_knotsArray[size() - 1]);
  }

  public double[] asArray() {
    return m_knotsArray.clone();
  }

  private static void validateValue(double _value) {
    if (Double.isInfinite(_value) || Double.isNaN(_value)) {
      throw new IllegalArgumentException("Knots value must not be infinite or NaN.");
    }
  }

  private Knots(double... _initialValues) {
    m_knotsArray = _initialValues;
  }

  @Override
  public boolean equals(Object obj) {
    var other = (Knots)obj;

    return m_knotsArray.equals(other.m_knotsArray);
  }

  @Override
  public String toString() {
    return Arrays.toString(m_knotsArray);
  }

  @Override
  public Knots clone() {
    return Knots.create(asArray());
  }

  private double[] m_knotsArray;
}
