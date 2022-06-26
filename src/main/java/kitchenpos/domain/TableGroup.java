package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {
    private static final int MINIMUM_GROUP_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        validateTables(orderTables);
        orderTables.forEach(table -> addOrderTable(table));
    }

    public TableGroup(final Long id, final List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    private void validateTables(final List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_GROUP_TABLE_SIZE) {
            throw new IllegalArgumentException("테이블 수가 2개 이상이어야 단체 지정할 수 있습니다");
        }

        if (orderTables.stream().anyMatch(table -> !table.isEmpty())) {
            throw new IllegalArgumentException("빈 테이블들만 단체 지정할 수 있습니다.");
        }

        if (orderTables.stream().anyMatch(OrderTable::hasGroup)) {
            throw new IllegalArgumentException("이미 단체 지정된 테이블이 있어서 단체 지정할 수 없습니다.");
        }
    }

    private void addOrderTable(final OrderTable table) {
        this.orderTables.add(table);
        table.setTableGroup(this);
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

    public void ungroup() {
        orderTables.forEach(table -> table.setTableGroup(null));
    }
}
