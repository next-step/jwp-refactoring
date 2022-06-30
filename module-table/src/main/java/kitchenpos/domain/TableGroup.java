package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TableGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Embedded
    private OrderTablesManager orderTables = new OrderTablesManager();

    public TableGroup() {
    }

    public Long getId() {
        return id;
    }

    public void mapOrderTables(List<OrderTable> orderTables) {
        this.orderTables.mapOrderTables(orderTables);
    }


}
