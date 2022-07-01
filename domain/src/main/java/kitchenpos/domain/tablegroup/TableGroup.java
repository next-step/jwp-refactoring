package kitchenpos.domain.tablegroup;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(OrderTables orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹은 2개 이상의 주문 테이블을 포함해야 합니다.");
        }
        for (OrderTable orderTable : orderTables) {
            orderTable.belongTo(this.id);
            add(orderTable);
        }
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getAll();
    }

    private void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    public void unGroup() {
        for (final OrderTable orderTable : this.orderTables) {
            orderTable.unGroup();
        }
    }
}
