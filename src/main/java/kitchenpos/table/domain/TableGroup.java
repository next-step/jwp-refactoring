package kitchenpos.table.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    private static final int MINIMUM_COUNT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {}

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        checkValue(orderTables);

        this.id = id;
        this.createdDate = createdDate;
        this.orderTables.addAll(orderTables);

        orderTables.forEach(orderTable -> orderTable.joinGroup(this));
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(null, createdDate, orderTables);
    }

    public TableGroup(OrderTables orderTables) {
        this(null, LocalDateTime.now(), orderTables.getValue());
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::leaveGroup);
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

    private void checkValue(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_COUNT) {
            throw new IllegalArgumentException("단체 지정에 속한 주문 테이블의 수는 최소 2개 이상이어야 합니다.");
        }
        orderTables.forEach(this::canBelongGroupByOrderTable);
    }

    private void canBelongGroupByOrderTable(OrderTable orderTable) {
        if (orderTable.isNotEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블만 새로운 그룹에 속할 수 있습니다.");
        }
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException("이미 단체 지정에 속한 주문 테이블이 존재합니다.");
        }
    }
}
