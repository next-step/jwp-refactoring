package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.table.TableGroup;

public class TableGroupDto {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableDto> orderTables;

    protected TableGroupDto() {
    }

    private TableGroupDto(Long id, LocalDateTime createdDate, List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto of(Long id, LocalDateTime createdDate, List<OrderTableDto> orderTables) {
        return new TableGroupDto(id, createdDate, orderTables);
    }

    public static TableGroupDto of(TableGroup tableGroup) {
        List<OrderTableDto> tempOrderTables = tableGroup.getOrderTables().stream()
                                                        .map(item -> OrderTableDto.of(item.getId(), item.getTableGroup().getId(), item.getNumberOfGuests(), item.getEmpty()))
                                                        .collect(Collectors.toList());

        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(), tempOrderTables);
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return this.orderTables;
    }

}
