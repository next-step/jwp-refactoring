package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "table_group")
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Embedded
    private OrderTableBag orderTableBag;

    private TableGroup(LocalDateTime createdDate, OrderTableBag orderTableBag) {
        this.createdDate = createdDate;
        this.orderTableBag = orderTableBag;
    }

    public static TableGroup of(LocalDateTime createdDate, OrderTableBag orderTables) {
        return new TableGroup(createdDate, orderTables);
    }

    public TableGroup() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public OrderTableBag getOrderTableBag() {
        return orderTableBag;
    }

    public boolean hasSameOrderTableSize(List<OrderTable> orderTables) {
        return orderTableBag.sameSize(orderTables.size());
    }

    public List<OrderTable> orderTables() {
        return orderTableBag.getOrderTableList();
    }

    public List<Long> tableIds() {
        return this.orderTableBag.orderTableIds();
    }

    public void updateTableGroup() {
        this.orderTableBag.updateTableGroup(this);
    }

    public void unGroup() {
        this.orderTableBag.unGroup();
    }

    public List<Long> orderTableIds() {
        return this.orderTableBag.orderTableIds();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroup that = (TableGroup) o;
        return Objects.equals(orderTableBag, that.orderTableBag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableBag);
    }
}
