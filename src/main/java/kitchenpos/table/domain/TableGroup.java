package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableGroup {
    public static final String ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE = "주문 테이블이 비어있을 수 없다.";
    public static final String ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE = "주문 테이블의 갯수가 2보다 작을 수 없다.";
    public static final int MINIMUM_SIZE = 2;
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException(ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE);
        }
        if (orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
        }
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
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

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
