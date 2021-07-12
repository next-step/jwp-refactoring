package kitchenpos.table.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.domain.OrderTableEntity;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableRequest {

  private Integer numberOfGuests;
  private Boolean empty;

  @JsonCreator
  public TableRequest(@JsonProperty("numberOfGuests") Integer numberOfGuests,
                      @JsonProperty("empty") Boolean empty) {
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public Integer getNumberOfGuests() {
    return numberOfGuests;
  }

  public Boolean isEmpty() {
    return empty;
  }

  public OrderTableEntity toEntity() {
    return new OrderTableEntity(numberOfGuests, empty);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TableRequest that = (TableRequest) o;
    return Objects.equals(numberOfGuests, that.numberOfGuests) && Objects.equals(empty, that.empty);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numberOfGuests, empty);
  }
}
