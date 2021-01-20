package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public void add(Long tableGroupId, List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }

            orderTable.changeEmpty(false);
            orderTable.changeTableGroupId(tableGroupId);
            this.orderTables.add(orderTable);
        }
    }
}
