package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTableEntity;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableResponse {

  private final Long id;
  private final Long tableGroupId;
  private final int numberOfGuests;
  private final boolean empty;

  public TableResponse(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
    this.id = id;
    this.tableGroupId = tableGroupId;
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public static TableResponse from(OrderTableEntity table) {
    return new TableResponse(table.getId(), table.getTableGroupId(), table.getNumberOfGuests(), table.isEmpty());
  }

  public static List<TableResponse> ofList(List<OrderTableEntity> tables) {
    return tables.stream()
            .map(TableResponse::from)
            .collect(Collectors.toList());
  }

  public Long getId() {
    return id;
  }

  public Long getTableGroupId() {
    return tableGroupId;
  }

  public int getNumberOfGuests() {
    return numberOfGuests;
  }

  public boolean isEmpty() {
    return empty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TableResponse that = (TableResponse) o;
    return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tableGroupId, numberOfGuests, empty);
  }
}
