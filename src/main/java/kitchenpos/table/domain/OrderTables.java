package kitchenpos.table.domain;

import kitchenpos.table.exception.OrderTableException;
import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "table_group", fetch = FetchType.LAZY)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        //orderTableValid(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

//    private void orderTableValid(List<OrderTable> orderTables) {
//        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
//            throw new OrderTableException(OrderTableException.ORDER_TABLE_SIZE_OVER_TWO_MSG);
//        }
//
//        if (orderTables.stream()
//                    .map(OrderTable::getTableGroupId)
//                    .anyMatch(Objects::nonNull)) {
//            throw new OrderTableException(OrderTableException.ORDER_TALBE_ALREADY_HAS_GROUP_MSG);
//        }
//    }

    public List<Long> orderTableIds() {
        return orderTables.stream()
                            .map(OrderTable::getId)
                            .collect(Collectors.toList());
    }

    public void unGroup() {
        orderTables.forEach(OrderTable::unGroup);
    }
}
