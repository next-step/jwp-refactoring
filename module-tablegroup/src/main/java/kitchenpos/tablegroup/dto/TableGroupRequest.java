package kitchenpos.tablegroup.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TableGroupRequest {

  private final List<TableGroupId> orderTables;

  @JsonCreator
  public TableGroupRequest(@JsonProperty("orderTables") List<TableGroupId> orderTables) {
    this.orderTables = orderTables;
  }

  public List<TableGroupId> getOrderTables() {
    return orderTables;
  }

  public List<Long> extractTableIds() {
    return orderTables.stream()
            .map(TableGroupId::getId)
            .collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TableGroupRequest that = (TableGroupRequest) o;
    return Objects.equals(orderTables, that.orderTables);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderTables);
  }

  public static class TableGroupId {
    private final Long id;

    @JsonCreator
    public TableGroupId(@JsonProperty("id") Long id) {
      this.id = id;
    }

    public Long getId() {
      return id;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TableGroupId that = (TableGroupId) o;
      return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id);
    }
  }
}
