package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTableBag {

    @JoinColumn(name = "table_group_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderTable> orderTableList;

    private OrderTableBag(List<OrderTable> orderTableList) {
        this.orderTableList = orderTableList;
    }

    public static OrderTableBag from(List<OrderTable> orderTableList) {
        return new OrderTableBag(orderTableList);
    }

    protected OrderTableBag() {
    }

    public List<Long> orderTableIds() {
        return this.orderTableList.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTableList() {
        return orderTableList;
    }

    public boolean sameSize(int target) {
        return this.orderTableList.size() == target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTableBag that = (OrderTableBag) o;
        return Objects.equals(orderTableList, that.orderTableList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTableList);
    }
}
