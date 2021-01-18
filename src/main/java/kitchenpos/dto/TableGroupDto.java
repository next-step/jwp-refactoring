package kitchenpos.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupDto {

    private Long id;

    private LocalDateTime createdDate;

    private List<OrderTableDto> orderTables;

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }

    public TableGroupDto() {
    }

    public TableGroupDto(LocalDateTime createdDate,
            List<OrderTableDto> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroupDto(Long id, LocalDateTime createdDate,
            List<OrderTableDto> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupDto of(TableGroup tableGroup, List<OrderTable> orderTables) {
        List<OrderTableDto> collect = orderTables.stream()
                .map(OrderTableDto::of)
                .collect(Collectors.toList());

        return new TableGroupDto(tableGroup.getId(), tableGroup.getCreatedDate(), collect);
    }

    public TableGroup toEntity() {
        if (createdDate == null) {
            createdDate = LocalDateTime.now();
        }
        return new TableGroup(createdDate);
    }
}
