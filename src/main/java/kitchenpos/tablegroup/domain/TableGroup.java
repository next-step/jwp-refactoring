package kitchenpos.tablegroup.domain;


import kitchenpos.ordertable.domain.OrderTable;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    private static final int MIN_GROUP_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = new OrderTables(orderTables, this);
    }

    public TableGroup(final Long id, final List<OrderTable> orderTables) {
        validate(orderTables);
        this.id = id;
        this.orderTables = new OrderTables(orderTables, this);;
    }

    private void validate(final List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_GROUP_SIZE) {
            throw new IllegalArgumentException(String.format("최소 %d개 이상 단체 지정 할 수 있습니다.", MIN_GROUP_SIZE));
        }

        if (hasNotEmptyTable(orderTables)) {
            throw new IllegalArgumentException("주문 테이블이 등록 가능 상태면 단체 지정할 수 없습니다.");
        }

        if (isAssigned(orderTables)) {
            throw new IllegalArgumentException("이미 단체 지정이 되어 있으면 지정할 수 없다.");
        }
    }

    private boolean isAssigned(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroupId()));
    }

    private boolean hasNotEmptyTable(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }
}
