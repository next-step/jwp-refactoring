package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

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

    @OneToMany(mappedBy = "tableGroupId")
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체테이블은 최소 2테이블부터 구성할수 있습니다.");
        }

        if (isValidation(orderTables)) {
            throw new IllegalArgumentException("빈테이블 또는 이미 단체테이블인 테이블이 존재하는 경우는 단체테이블을 구성할수 없습니다.");
        }

        this.createdDate = createdDate;
        this.orderTables = orderTables;

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
        return orderTables;
    }
}
