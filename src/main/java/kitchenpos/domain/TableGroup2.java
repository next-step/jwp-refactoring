package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "table_group")
public class TableGroup2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable2> orderTables = new ArrayList<>();

    protected TableGroup2() {
    }

    public TableGroup2(List<OrderTable2> orderTables) {
        this.createdDate = LocalDateTime.now();
        addOrderTables(orderTables);
    }

    private void addOrderTables(List<OrderTable2> orderTables) {
        orderTables.forEach(this::addOrderTable);
    }

    private void addOrderTable(OrderTable2 orderTable) {
        if (!orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
        orderTable.setTableGroup(this);
    }

    public List<OrderTable2> getOrderTables() {
        return orderTables;
    }
}
