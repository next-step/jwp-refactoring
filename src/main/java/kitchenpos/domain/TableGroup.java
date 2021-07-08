package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, orderTables);
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        checkOrderTables(orderTables);
        this.id = id;
        orderTables.forEach(this::addOrderTable);
    }

    public void checkOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블을 그룹화 하려면 2개 이상의 테이블이 필요합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    private void addOrderTable(OrderTable orderTable) {
        orderTables.add(orderTable);
        orderTable.setTableGroup(this);
    }

    public void ungroup() {
        orderTables.ungroup();
    }
}
