package kitchenpos.table.domain;

import kitchenpos.table.exception.UnableCreateTableGroupException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        if (isValidation(orderTables)) {
            throw new UnableCreateTableGroupException("빈테이블 또는 이미 단체테이블인 테이블이 존재하는 경우는 단체테이블을 구성할수 없습니다.");
        }

        this.orderTables = new OrderTables(orderTables);
        this.createdDate = createdDate;

    }

    private boolean isValidation(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> orderTable.isUnableTableGroup());
    }


    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }
}
