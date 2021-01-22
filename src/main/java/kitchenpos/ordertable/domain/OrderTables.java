package kitchenpos.ordertable.domain;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "orderTableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables, int requestSize) {
        validate(orderTables, requestSize);
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void setOrderTableGroup(OrderTableGroup orderTableGroup) {
        this.orderTables.forEach(orderTable -> orderTable.setOrderTableGroup(orderTableGroup));
    }

    private void validate(List<OrderTable> orderTables, int requestSize) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 목록이 2개 이상이어야 한다.");
        }
        if (orderTables.size() != requestSize) {
            throw new IllegalArgumentException("테이블 목록이 등록되어 있어야 한다.");
        }
        for (OrderTable orderTable : orderTables) {
            if (orderTable.isNotAvailableOrderTable()) {
                throw new IllegalArgumentException("테이블 목록이 이미 다른 테이블 그룹에 속해있지 않아야 한다.");
            }
        }
    }
}
