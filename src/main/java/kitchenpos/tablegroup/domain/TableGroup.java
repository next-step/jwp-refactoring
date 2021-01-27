package kitchenpos.tablegroup.domain;

import kitchenpos.advice.exception.OrderTableException;
import kitchenpos.order.domain.OrderTable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void updateOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.addTableGroup(this.id));
        this.orderTables = orderTables;
    }

    public void ungroup() {
        orderTables.stream()
                .forEach(OrderTable::ungroup);
        this.orderTables = Collections.EMPTY_LIST;
    }
    public void validateEqualOrderTableSize(int size) {
        if (orderTables.size() != size) {
            throw new OrderTableException("요청 주문 테이블 사이즈와 저장된 주문 테이블 사이즈가 다릅니다");
        }
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", orderTables=" + orderTables +
                '}';
    }
}
