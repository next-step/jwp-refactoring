package kitchenpos.table.dto;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.TableGroup;

import static java.util.stream.Collectors.toList;

public class TableGroupDto {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    public TableGroupDto() { }

    public TableGroupDto(List<OrderTableDto> orderTables) {
        this(null, null, orderTables);
    }

    public TableGroupDto(Long id, LocalDateTime createdDate, List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto of(TableGroup tableGroup) {
        return new TableGroupDto(tableGroup.getId(),
                                 tableGroup.getCreatedDate(),
                                 tableGroup.getOrderTables()
                                           .getData()
                                           .stream()
                                           .map(OrderTableDto::of)
                                           .collect(toList()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
