package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
class Price implements Comparable<Price> {

  static final Price REDUCE_IDENTITY = new Price(BigDecimal.ZERO);

  private static final double MIN_PRICE = 0D;
  private static final String PRICE_SHOULD_EQUAL_OR_LARGER_THAN_MIN_PRICE = "가격은 %f 이상이어야 합니다.";

  @Column(name = "price", precision = 19, scale = 2, nullable = false)
  private BigDecimal value;

  protected Price() {
  }

  private Price(BigDecimal value) {
    this.value = value;
  }

  public static Price from(Double price) {
    validateDoubleValue(price);
    return new Price(BigDecimal.valueOf(price));
  }

  public static Price from(BigDecimal price) {
    validateBigDecimalValue(price);
    return new Price(price);
  }

  private static void validateDoubleValue(Double price) {
    if (Objects.isNull(price) || price < MIN_PRICE) {
      throw new IllegalArgumentException(String.format(PRICE_SHOULD_EQUAL_OR_LARGER_THAN_MIN_PRICE, MIN_PRICE));
    }
  }

  private static void validateBigDecimalValue(BigDecimal price) {
    if (Objects.isNull(price) || price.doubleValue() < MIN_PRICE) {
      throw new IllegalArgumentException(String.format(PRICE_SHOULD_EQUAL_OR_LARGER_THAN_MIN_PRICE, MIN_PRICE));
    }
  }

  public BigDecimal getValue() {
    return value;
  }
  public Price sum(Price price2) {
    return Price.from(this.value.add(price2.value));
  }

  @Override
  public int compareTo(Price other) {
    return this.value.compareTo(other.value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Price price = (Price) o;
    return Objects.equals(value, price.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
