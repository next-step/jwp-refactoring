package kitchenpos.table.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.exception.BadRequestException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup extends BaseEntity {
    private static final int MINIMUM_GROUP_SIZE = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public static TableGroup create() {
        return new TableGroup();
    }

    public void group(List<OrderTable> orderTables) {
        checkOrderTablesValidation(orderTables);

        for (OrderTable orderTable : orderTables) {
            addOrderTable(orderTable);
        }

        this.orderTables.group(this);
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    private void addOrderTable(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    private void checkOrderTablesValidation(List<OrderTable> orderTables) {
        if (orderTables.isEmpty()) {
            throw new BadRequestException("주문 테이블이 존재하지 않아 그룹화할 수 없습니다.");
        }

        if (orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new BadRequestException("주문 테이블 " + MINIMUM_GROUP_SIZE + "개 이상 그룹화할 수 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.asList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
