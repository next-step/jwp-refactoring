package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "table_group")
public class TableGroupEntity {
    private static final int MINIMUM_GROUP_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTableEntity> orderTables = new ArrayList<>();

    protected TableGroupEntity() {
    }

    public TableGroupEntity(List<OrderTableEntity> orderTables) {
        validateTables(orderTables);
        orderTables.forEach(table -> addOrderTable(table));
    }

    private void validateTables(List<OrderTableEntity> orderTables) {
        if (orderTables.size() < MINIMUM_GROUP_TABLE_SIZE) {
            throw new IllegalArgumentException("테이블 수가 2개 이상이어야 단체 지정할 수 있습니다");
        }

        if (orderTables.stream().anyMatch(table -> !table.isEmpty())) {
            throw new IllegalArgumentException("빈 테이블들만 단체 지정할 수 있습니다.");
        }

        if (orderTables.stream().anyMatch(OrderTableEntity::hasGroup)) {
            throw new IllegalArgumentException("이미 단체 지정된 테이블이 있어서 단체 지정할 수 없습니다.");
        }
    }

    private void addOrderTable(OrderTableEntity table) {
        this.orderTables.add(table);
        table.setTableGroup(this);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableEntity> getOrderTables() {
        return orderTables;
    }
}
