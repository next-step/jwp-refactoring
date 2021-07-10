package kitchenpos.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.MERGE)
    private final List<OrderTable> orderTables;

    public OrderTables() {
        orderTables = new ArrayList<>();
    }

    public List<OrderTable> list() {
        return Collections.unmodifiableList(orderTables);
    }

    public void changeEmpty(final boolean empty) {
        orderTables.stream()
                .forEach(orderTable -> orderTable.changeEmpty(empty));
    }

    public void append(final TableGroup tableGroup, final OrderTable ...orderTableArray) {
        List<OrderTable> orderTables = Arrays.stream(orderTableArray)
            .filter(Objects::nonNull)
            .distinct().collect(Collectors.toList());

        validation(orderTables);

        orderTables.stream().forEach(orderTable -> {
            orderTable.setTableGroup(tableGroup);

            this.orderTables.add(orderTable);
        });
    }

    private void validation(final List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("주문테이블 최소 갯수는 2개입니다.");
        }

        if (isNotEmpty(orderTables) || alreadyRegisteredTableGroup(orderTables)) {
            throw new IllegalArgumentException("테이블이 비어있지 않습니다.");
        }
    }

    private boolean alreadyRegisteredTableGroup(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> Objects.nonNull(orderTable.getTableGroup()));
    }

    private boolean isNotEmpty(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());
    }

    public void clearTableGroup() {
        orderTables.stream().forEach(OrderTable::clearTableGroup);
    }
}
