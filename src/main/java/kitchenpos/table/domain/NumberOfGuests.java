package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {

  private static final int MINIMUM_NUMBER_OF_GUEST = 0;

  @Column(name = "number_of_guests", precision = 11, nullable = false)
  private Integer value;

  protected NumberOfGuests() {
  }

  private NumberOfGuests(Integer value) {
    this.value = value;
  }

  public static NumberOfGuests from(int value) {
    validateValue(value);
    return new NumberOfGuests(value);
  }

  private static void validateValue(int value) {
    if (value < MINIMUM_NUMBER_OF_GUEST) {
      throw new IllegalArgumentException();
    }
  }

  public Integer getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NumberOfGuests that = (NumberOfGuests) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }
}
