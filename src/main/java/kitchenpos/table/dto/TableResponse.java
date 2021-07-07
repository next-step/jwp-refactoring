package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTableEntity;

public class TableResponse {

  private final long id;
  private final long tableGroupId;
  private final int numberOfGuests;
  private final boolean empty;

  public TableResponse(long id, long tableGroupId, int numberOfGuests, boolean empty) {
    this.id = id;
    this.tableGroupId = tableGroupId;
    this.numberOfGuests = numberOfGuests;
    this.empty = empty;
  }

  public static TableResponse from(OrderTableEntity table) {
    return new TableResponse(table.getId(), table.getTableGroupId(), table.getNumberOfGuests(), table.isEmpty());
  }
}
