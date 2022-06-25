package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.BaseEntity;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    public void addOrderTable(OrderTable orderTable) {
        orderTables.add(orderTable);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
