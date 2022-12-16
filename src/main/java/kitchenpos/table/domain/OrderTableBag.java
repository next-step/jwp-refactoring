package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTableBag {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY)
    private List<OrderTable> orderTableList;

    public OrderTableBag(List<OrderTable> orderTableList) {
        checkCreatable(orderTableList);
        this.orderTableList = orderTableList;
    }

    public static OrderTableBag from(List<OrderTable> orderTableList) {
        checkCreatable(orderTableList);
        return new OrderTableBag(orderTableList);
    }

    protected OrderTableBag() {
    }

    private static void checkCreatable(List<OrderTable> orderTableList) {
        validTableListSize(orderTableList);
        validEmptyTable(orderTableList);
        validNonNullTableGroupId(orderTableList);
    }

    private static void validTableListSize(List<OrderTable> orderTableList) {
        if (orderTableList.isEmpty() || orderTableList.size() < 2) {
            throw new IllegalArgumentException("한 개 이상의 테이블이 있어야 합니다");
        }
    }

    private static void validEmptyTable(List<OrderTable> orderTableList) {
        if (emptyTable(orderTableList)) {
            throw new IllegalArgumentException("단체 지정 주문 테이블에 비어 있는 주문 테이블이 포함 되어 있습니다");
        }
    }

    private static boolean emptyTable(List<OrderTable> orderTableList) {
        return orderTableList.stream().anyMatch(OrderTable::isEmpty);
    }

    private static void validNonNullTableGroupId(List<OrderTable> orderTableList) {
        if (checkNullTableGroupId(orderTableList)) {
            throw new IllegalArgumentException("단체 지정 값이 없는 주문 테이블이 포함되어 있습니다");
        }
    }

    private static boolean checkNullTableGroupId(List<OrderTable> orderTableList) {
        return orderTableList.stream().anyMatch(it -> Objects.nonNull(it.getTableGroup()));
    }

    public void updateTableGroup(TableGroup tableGroup) {
        this.orderTableList.forEach(it -> it.updateGroup(tableGroup));
    }

    public void unGroup() {
        this.orderTableList.forEach(OrderTable::unGroup);
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
