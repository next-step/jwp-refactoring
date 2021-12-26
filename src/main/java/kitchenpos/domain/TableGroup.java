package kitchenpos.domain;

import kitchenpos.exception.InvalidTableGroupException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    private static final int MINIMUM_GROUP_SIZE = 2;
    private static final String INVALID_TABLE_GROUP = "오더테이블이 존재하지 않아 그룹화할 수 없습니다.";
    private static final String INVALID_TABLE_GROUP_SIZE = "오더 테이블이 하나인 경우 그룹화할 수 없습니다.";
    private static final String INVALID_UN_GROUP = "조리중이거나 요리중인 주문테이블에 테이블 그룹을 해제할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        validate(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        if (orderTables.isEmpty()) {
            throw new InvalidTableGroupException(INVALID_TABLE_GROUP);
        }

        if (orderTables.size() < MINIMUM_GROUP_SIZE) {
            throw new InvalidTableGroupException(TableGroup.INVALID_TABLE_GROUP_SIZE);
        }
    }

    public static TableGroup of(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(null, null, orderTables);
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

    public void validateUngroup() {
        if (!isCompleted()) {
            throw new InvalidTableGroupException(INVALID_UN_GROUP);
        }
    }

    private boolean isCompleted() {
        return orderTables.stream()
                .allMatch(orderTable -> orderTable.isCompleteOrders());
    }
}
