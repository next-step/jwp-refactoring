package kitchenpos.ordertable.domain;

import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderTables {
    public static final int MINIMUM_GROUP_NUMBER = 2;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup", cascade = CascadeType.PERSIST)
    private List<OrderTable> elements = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        elements = new ArrayList<>(orderTables);
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_GROUP_NUMBER) {
            throw new IllegalArgumentException("2개 이상의 테이블을 단체 지정할 수 있습니다.");
        }
    }

    public List<Long> getOrderTableIds() {
        return elements.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> elements() {
        return Collections.unmodifiableList(elements);
    }

    public void validateTablesSize(int tableSize) {
        if (elements.size() != tableSize) {
            throw new IllegalArgumentException("중복된 테이블이 있거나 등록되지 않은 테이블이 있습니다.");
        }
    }

    public void validateCanGroup() {
        for (final OrderTable orderTable : elements) {
            orderTable.validateCanGroup();
        }
    }

    public void group(TableGroup tableGroup) {
        for (final OrderTable orderTable : elements) {
            orderTable.group(tableGroup);
        }
    }

    public void unGroup() {
        for (final OrderTable orderTable : elements) {
            orderTable.unGroup();
        }
    }
}
