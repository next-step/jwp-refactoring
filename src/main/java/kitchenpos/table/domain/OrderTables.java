package kitchenpos.table.domain;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderTable> orderTables;

    protected OrderTables() {}

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTableIsEmptyOrNonNull(orderTables);

        this.orderTables = new ArrayList<>(orderTables);
    }

    public static OrderTables from(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    private void validateOrderTableIsEmptyOrNonNull(List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.tableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<OrderTable> readOnlyValue() {
        return Collections.unmodifiableList(this.orderTables);
    }

}
