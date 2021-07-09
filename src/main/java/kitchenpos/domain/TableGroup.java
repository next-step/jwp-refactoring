package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @Transient
    private static final int TABLE_COUNT_MIN = 2;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {}

    public TableGroup(Long id) {
        this.id = id;
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this(orderTables);
        this.id = id;
    }

    public TableGroup(List<OrderTable> orderTables) {
        verifyAvailable(orderTables);
        group(orderTables);
    }

    public void group(List<OrderTable> orderTables) {
        orderTables.stream()
                .forEach(orderTable -> {
                    orderTable.changeTableGroup(this);
                    orderTable.setEmpty(false);
                });
        this.orderTables = orderTables;
    }

    public void ungroup() {
        verifyAvailableUngroup();
        orderTables.stream()
                .forEach(orderTable -> orderTable.changeTableGroup(null));
    }

    public void addOrderTable(OrderTable orderTable) {
        if (!orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
    }

    private void verifyAvailableUngroup() {
        if (orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isCompletionAllOrders())) {
            throw new IllegalArgumentException("계산완료가 안된 테이블이 존재합니다.");
        }
    }

    private void verifyAvailable(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < TABLE_COUNT_MIN) {
            throw new IllegalArgumentException("주문테이블이 " + TABLE_COUNT_MIN + "개 미만입니다.");
        }

        if (orderTables.stream()
                .filter(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup()))
                .findFirst()
                .isPresent()) {
            throw new IllegalArgumentException("주문테이블은 빈테이블이어야하고 단체지정이 되어있으면 안됩니다.");
        }
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

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
