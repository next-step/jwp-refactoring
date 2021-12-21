package kitchenpos.domain;

import kitchenpos.exception.IllegalOrderTableException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        // 테이블이 존재하지않거나 2개보다 작은 경우 예외처리
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalOrderTableException();
        }
        //테이블이 사용가능 테이블인경우 예외처리
        if (!targetTablesEmpty(orderTables)) {
            throw new IllegalOrderTableException();
        }
    }

    private boolean targetTablesEmpty(List<OrderTable> orderTables) {
        return orderTables.stream().allMatch(OrderTable::isEmpty);
    }

    private boolean targetTablesOrderFinished(List<OrderTable> orderTables) {
        return orderTables.stream().allMatch(OrderTable::isOrderFinished);
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
