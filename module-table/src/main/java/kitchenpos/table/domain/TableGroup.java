package kitchenpos.table.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup {
    private static final int MIN_TABLE_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate, OrderTables emptyTables) {
        this(null, createdDate, emptyTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, OrderTables emptyTables) {
        validate(emptyTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = emptyTables;
        emptyTables.changeOrderTable();
    }

    public void ungroupingTableGroup() {
        orderTables.clear();
    }

    private void validate(OrderTables tables) {
        if (tables.size() < MIN_TABLE_SIZE) {
            throw new IllegalArgumentException("[ERROR] 단체 지정에는 최소 2개의 테이블이 필요합니다.");
        }
        if (tables.containsOrderTable()) {
            throw new IllegalArgumentException("[ERROR] 주문 테이블 있는 경우 단체 지정 할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTables getOrderTables() {
        return orderTables;
    }

}
