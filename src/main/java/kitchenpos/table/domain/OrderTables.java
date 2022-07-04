package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    private static final int MINIMUM_TABLE = 2;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {

    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTablesCheck(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    private void validateOrderTablesCheck(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
        if (orderTables.size() < MINIMUM_TABLE) {
            throw new IllegalArgumentException("주문 테이블은 " + MINIMUM_TABLE + "개 이상 존재하여야 합니다.");
        }
    }
}
