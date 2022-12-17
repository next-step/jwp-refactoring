package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    public static final String ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE = "주문 테이블이 비어있을 수 없다.";
    public static final String ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE = "주문 테이블의 갯수가 2보다 작을 수 없다.";
    public static final int MINIMUM_SIZE = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
        }
        if (orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
        }
        for (final OrderTable savedOrderTable : orderTables) {
            if (!savedOrderTable.isEmpty()) {
                throw new IllegalArgumentException();
            }
            if (Objects.nonNull(savedOrderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
        this.createdDate = LocalDateTime.now();
        this.orderTables.addAll(orderTables);
    }

    public TableGroup(long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
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

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void upGroup() {
        this.orderTables.unGroup();
    }

    public List<Long> getOrderTableIds() {
        return this.orderTables.getOrderTableIds();
    }

    public List<OrderTable> getOrderTables() {
        return this.orderTables.getOrderTables();
    }

    public void setOrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
    }
}
