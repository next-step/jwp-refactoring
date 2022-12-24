package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {

    private static final int MIN_NUMBER_OF_GROUP = 2;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tableGroupId")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        validateTableGroup();
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.occupied();
        }
    }

    private void validateTableGroup() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_NUMBER_OF_GROUP) {
            throw new IllegalArgumentException("단체 지정할 테이블이 없거나 단체 지정 할 테이블 2개 미만 입니다.");
        }

        orderTables.stream()
            .filter(orderTable -> !orderTable.ableToGroup())
            .findFirst()
            .ifPresent(o -> {
                    throw new IllegalArgumentException("테이블이 비어있지 않거나, 이미 단체 지정된 테이블 입니다.");
                }
            );
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
