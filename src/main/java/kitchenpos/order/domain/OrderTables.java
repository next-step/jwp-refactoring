package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.generic.exception.NotEnoughTablesException;

@Embeddable
public class OrderTables {

    private static final int MINIMUM_TABLE_COUNT = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public static OrderTables of(OrderTable... orderTables) {
        return new OrderTables(Arrays.asList(orderTables));
    }

    public static OrderTables of(List<OrderTable> orderTables) {
        return new OrderTables(orderTables);
    }

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        checkArgument(orderTables);
        this.orderTables = orderTables;
    }

    private void checkArgument(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_TABLE_COUNT) {
            throw new NotEnoughTablesException("그룹화 하려면 %d개 이상의 테이블이 필요합니다.", MINIMUM_TABLE_COUNT);
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::leaveTableGroup);
    }

    public OrderTables withTableGroup(TableGroup tableGroup) {
        return new OrderTables(orderTables.stream()
            .map(orderTable -> orderTable.withTableGroup(tableGroup))
            .collect(Collectors.toList()));
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
