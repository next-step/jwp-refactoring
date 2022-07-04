package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableResponse;

@Entity
@Table(name = "table_group")
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public TableGroup(final Long id) {
        this.id = id;
    }

    public TableGroup(final OrderTables orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup(final Long id, final LocalDateTime createdDate, final OrderTables orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        this.orderTables.makeRelations(this, orderTables);
    }

    public void removeRelationsToOrderTables() {
        this.orderTables.removeRelations();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables.getResponses();
    }
}
