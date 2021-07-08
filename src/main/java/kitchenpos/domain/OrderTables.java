package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public void add(OrderTable orderTable) {
        checkOrderTable(orderTable);
        orderTable.changeEmpty(false);
        orderTables.add(orderTable);
    }

    private void checkOrderTable(OrderTable orderTable) {
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블만 그룹화 할 수 있습니다.");
        }

        if (orderTable.hasTableGroup()) {
            throw new IllegalArgumentException("테이블이 이미 그룹에 포함되어 있습니다.");
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public void forEach(Consumer<OrderTable> consumer) {
        orderTables.forEach(consumer);
    }

    public <T> List<T> mapList(Function<OrderTable, T> function) {
        return orderTables.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}
