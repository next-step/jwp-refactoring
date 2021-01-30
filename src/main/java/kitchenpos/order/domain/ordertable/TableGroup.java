package kitchenpos.order.domain.ordertable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public static TableGroup createTableGroup(final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.grouping(orderTables);
        return tableGroup;
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

    private void grouping(final List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
        this.orderTables.forEach(orderTable -> orderTable.putToTableGroup(this));
    }

    private void validate(final List<OrderTable> orderTables) {
        final int minimumGroupingOrderTableSize = 2;

        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("그룹으로 지정할 주문 테이블이 비어있습니다.");
        }

        if (orderTables.size() < minimumGroupingOrderTableSize) {
            throw new IllegalArgumentException("그룹으로 지정할 주문 테이블 최소 그룹 대상 갯수보다 적습니다.");
        }

        if (orderTables.stream().anyMatch(OrderTable::isOccupied)) {
            throw new IllegalArgumentException("비어있지 않은 테이블이 있습니다.");
        }

        if (orderTables.stream().anyMatch(OrderTable::isAlreadyGroup)) {
            throw new IllegalArgumentException("이미 단체 지정된 테이블이 있습니다.");
        }
    }

    public void ungroup() {
        if (hasCookingOrMealOrderTable()) {
            throw new IllegalArgumentException("조리 또는 식사 중인 주문 테이블이 존재합니다.");
        }
        orderTables.forEach(OrderTable::ungroup);
        orderTables = new ArrayList<>();
    }

    private boolean hasCookingOrMealOrderTable() {
        return this.orderTables.stream()
            .anyMatch(OrderTable::hasCookingOrMealOrder);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final TableGroup that = (TableGroup)o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
