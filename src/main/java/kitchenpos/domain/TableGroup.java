package kitchenpos.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "table_group")
public class TableGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 20)
    private Long id;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public void saveOrderTable(OrderTable orderTable) {
        orderTable.checkAvailabilityTableGroup();
        this.orderTables.add(orderTable);
        orderTable.addTableGroup(this);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }

    public static final class TableGroupBuilder {

        private TableGroupBuilder() {
        }

        public TableGroup build() {
            TableGroup tableGroup = new TableGroup();
            return tableGroup;
        }
    }
}
