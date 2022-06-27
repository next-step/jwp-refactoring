package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "table_group")
public class TableGroup {

    private static final int MIN_ORDER_TABLE_COUNT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        addOrderTables(orderTables);
    }

    public TableGroup(List<OrderTable> orderTables) {
        addOrderTables(orderTables);
    }

    private void addOrderTables(final List<OrderTable> orderTables) {
        validateAddingOrderTables(orderTables);
        addAll(orderTables);
    }

    private void addAll(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            add(orderTable);
        }
    }

    private void add(OrderTable orderTable) {
        if (!orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
        orderTable.attachToTableGroup(this);
    }

    public void ungroup() {
        orderTables.stream().forEach(OrderTable::detachFromTableGroup);
        orderTables.clear();
    }

    private void validateAddingOrderTables(final List<OrderTable> orderTables) {
        if (orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new IllegalArgumentException();
        }

        if (checkOrderTableEmptyOrInTableGroup(orderTables)) {
            throw new IllegalArgumentException("단체 지정을 위해서는 주문 테이블이 필요 합니다.");
        }
    }

    private boolean checkOrderTableEmptyOrInTableGroup(final List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty() || orderTable.isInTableGroup());
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
