package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
class Price {

  private static final double MIN_PRICE = 0D;
  private static final String PRICE_SHOULD_EQUAL_OR_LARGER_THAN_MIN_PRICE = "가격은 %f 이상이어야 합니다.";

  @Column(name = "price", precision = 19, scale = 2)
  private BigDecimal value;

  protected Price() {
  }

  private Price(BigDecimal value) {
    this.value = value;
  }

  public static Price from(Double price) {
    validatePriceValue(price);
    return new Price(BigDecimal.valueOf(price));
  }

  private static void validatePriceValue(Double price) {
    if (Objects.isNull(price) || price < MIN_PRICE) {
      throw new IllegalArgumentException(String.format(PRICE_SHOULD_EQUAL_OR_LARGER_THAN_MIN_PRICE, MIN_PRICE));
    }
  }

  public BigDecimal getValue() {
    return value;
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
