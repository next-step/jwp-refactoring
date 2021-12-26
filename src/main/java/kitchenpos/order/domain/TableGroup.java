package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정 시 주문테이블은 두 테이블 이상 이어야 합니다.");
        }
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
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
}
