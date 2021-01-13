package kitchenpos.domain.tablegroup;

import kitchenpos.domain.tablegroup.exceptions.InvalidTableGroupTryException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTableInTableGroups {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "table_group_id")
    private List<OrderTableInTableGroup> orderTableInTableGroups;

    protected OrderTableInTableGroups() {
    }

    public OrderTableInTableGroups(final List<OrderTableInTableGroup> orderTableInTableGroups) {
        validate(orderTableInTableGroups);
        this.orderTableInTableGroups = orderTableInTableGroups;
    }

    public List<OrderTableInTableGroup> getList() {
        return new ArrayList<>(orderTableInTableGroups);
    }

    private void validate(final List<OrderTableInTableGroup> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidTableGroupTryException("2개 미만의 주문 테이블로 단체 지정할 수 없다.");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderTableInTableGroups that = (OrderTableInTableGroups) o;
        return Objects.equals(orderTableInTableGroups, that.orderTableInTableGroups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableInTableGroups);
    }

    @Override
    public String toString() {
        return "OrderTableInTableGroups{" +
                "orderTableInTableGroups=" + orderTableInTableGroups +
                '}';
    }
}
