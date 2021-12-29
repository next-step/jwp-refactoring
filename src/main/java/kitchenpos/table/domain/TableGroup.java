package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정할 주문 테이블은 2개 이상이어야 합니다.");
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.existTableGroup()) {
                throw new IllegalArgumentException("주문 테이블이 빈 테이블이 아니거나 이미 단체 지정이 되어있습니다.");
            }
        }

        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), orderTables);
        for (OrderTable orderTable : orderTables) {
            orderTable.applyTableGroup(tableGroup);
        }
        return tableGroup;
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
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
}
