package kitchenpos.tablegroup.dto;

import kitchenpos.table.domain.OrderTableEntity;
import kitchenpos.table.dto.TableResponse;
import kitchenpos.tablegroup.domain.TableGroupEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TableGroupResponse {
  private final long id;
  private final LocalDateTime createdDate;
  private final List<TableResponse> orderTables;

  public TableGroupResponse(long id, LocalDateTime createdDate, List<TableResponse> orderTables) {
    this.id = id;
    this.createdDate = createdDate;
    this.orderTables = orderTables;
  }

  public static TableGroupResponse of(TableGroupEntity savedGroup, List<OrderTableEntity> savedOrderTables) {
    return new TableGroupResponse(savedGroup.getId(), savedGroup.getCreatedDate(), TableResponse.ofList(savedOrderTables));
  }

  public long getId() {
    return id;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public List<TableResponse> getOrderTables() {
    return orderTables;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TableGroupResponse that = (TableGroupResponse) o;
    return id == that.id && Objects.equals(createdDate, that.createdDate) && Objects.equals(orderTables, that.orderTables);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, createdDate, orderTables);
  }
}
