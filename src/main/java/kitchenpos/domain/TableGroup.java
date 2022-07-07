package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {
    private static Integer MIN_ORDER_TABLE_SIZE = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void checkOrderTablesEmptyOrSizeOne() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    public void checkAllOrderSavedOrderTables(final List<OrderTable> savedOrderTables) {
        checkOrderTablesSizeEqualTo(savedOrderTables);
        checkSavedOrderTablesEmptyAndNotGrouped(savedOrderTables);
    }

    private void checkOrderTablesSizeEqualTo(final List<OrderTable> savedOrderTables) {
        if (orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkSavedOrderTablesEmptyAndNotGrouped(List<OrderTable> savedOrderTables) {
        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void updateOrderTables() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(this);
            orderTable.changeEmpty(false);
        }
    }
}
