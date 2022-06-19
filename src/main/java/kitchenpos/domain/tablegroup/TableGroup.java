package kitchenpos.domain.tablegroup;

import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.domain.table.OrderTable;

@Entity
@Table(name = "table_group")
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = Lists.newArrayList();

    protected TableGroup() {}

    private TableGroup(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        this.createdDate = LocalDateTime.now();
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    public static TableGroup of(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
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

    public void assignedOrderTables(List<OrderTable> orderTables) {
        this.orderTables.addAll(orderTables);
        orderTables.forEach(orderTable -> orderTable.mappedByTableGroup(this));
    }
}
