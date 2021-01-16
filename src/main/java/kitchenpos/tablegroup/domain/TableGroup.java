package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {
    }

    public TableGroup(OrderTables orderTables) {
        validate(orderTables);
        this.orderTables = new OrderTables(orderTables.getOrderTables(), this);
    }

    public void ungroup() {
        getOrderTables().forEach(OrderTable::ungroup);
    }

    private void validate(OrderTables orderTables) {
        if (orderTables.isEmptyTablesNotMore(2)) {
            throw new IllegalArgumentException("2 개 이상의 빈 테이블이 없습니다.");
        }

        if (orderTables.hasOtherOrderTable()) {
            throw new IllegalArgumentException("단체 지정은 중복될 수 없습니다.");
        }
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
