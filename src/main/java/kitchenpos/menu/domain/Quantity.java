package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
class Quantity {

  private static final long MIN_QUANTITY = 1L;
  private static final String QUANTITY_SHOULD_EQUAL_OR_LARGER_THAN_MIN_QUANTITY = "수량은 %d 이상이어야 합니다.";

  @Column(name = "quantity", nullable = false)
  private long value;

  protected Quantity() {
  }

  private Quantity(long value) {
    this.value = value;
  }

  public static Quantity from(long value) {
    validateQuantityValue(value);
    return new Quantity(value);
  }

  public long getValue() {
    return value;
  }

  public BigDecimal getBigDecimalValue() {
    return BigDecimal.valueOf(value);
  }

  private static void validateQuantityValue(long value) {
    if (value < MIN_QUANTITY) {
      throw new IllegalArgumentException(String.format(QUANTITY_SHOULD_EQUAL_OR_LARGER_THAN_MIN_QUANTITY, MIN_QUANTITY));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Quantity quantity = (Quantity) o;
    return value == quantity.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
