package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    public static final String ORDER_TABLE_NOT_EMPTY_EXCEPTION_MESSAGE = "주문 테이블이 비어있을 수 없다.";
    public static final String ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE = "주문 테이블의 갯수가 2보다 작을 수 없다.";
    public static final int MINIMUM_SIZE = 2;
    public static final String ORDER_TABLE_EMPTY_EXCEPTION_MESSAGE = "주문 테이블의 상태가 empty 가 아니면 안된다.";
    public static final String TABLE_GROUP_NOT_NULL_EXCEPTION_MESSAGE = "테이블 그룹이 있을 수 없다.";

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {

    }

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    public void unGroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroup(null);
        }
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return this.orderTables;
    }

    public void mapTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : this.orderTables) {
            orderTable.mapTableGroup(tableGroup);
        }
    }

    private static void validate(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        for (final OrderTable orderTable : orderTables) {
            validateNotEmptyOrderTable(orderTable);
        }
    }

    private static void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException(ORDER_TABLE_MINIMUM_SIZE_EXCEPTION_MESSAGE);
        }
    }

    private static void validateNotEmptyOrderTable(OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException(ORDER_TABLE_EMPTY_EXCEPTION_MESSAGE);
        }
    }
}
