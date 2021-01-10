package kitchenpos.domain.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany
    @JoinColumn(name = "table_group_id")
    private List<OrderTableInTableGroup> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    TableGroup(final Long id, final LocalDateTime createdDate, final List<OrderTableInTableGroup> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(final LocalDateTime createdDate, final List<OrderTableInTableGroup> orderTables) {
        this(null, createdDate, orderTables);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableInTableGroup> getOrderTables() {
        return orderTables;
    }
}
