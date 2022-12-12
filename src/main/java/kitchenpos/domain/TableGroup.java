package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds(){
        return this.orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void validateOrderTables() {
        if (isOrderTablesEmpty() || isLessThanTwo()) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isOrderTablesEmpty(){
        return CollectionUtils.isEmpty(this.orderTables);
    }

    private boolean isLessThanTwo(){
        return this.orderTables.size() < 2;
    }

    public void validateOrderTablesSize(int savedOrderTablesSize) {
        if (this.orderTables.size() != savedOrderTablesSize) {
            throw new IllegalArgumentException();
        }
    }
}
