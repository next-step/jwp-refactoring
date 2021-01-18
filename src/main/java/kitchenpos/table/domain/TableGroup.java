package kitchenpos.table.domain;

import kitchenpos.common.BaseEntity;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTables = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        orderTables.forEach(this::addTable);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void addTable(OrderTable table) {
        checkAddTableValidation(table);
        table.saveOrderTable(this);
        orderTables.add(table);
    }

    private void checkAddTableValidation(OrderTable table) {
        if (!table.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있지 않습니다.");
        }

        if (table.hasTableGroup()) {
            throw new IllegalArgumentException("테이블에 단체가 지정 되어 있습니다.");
        }
    }
}
