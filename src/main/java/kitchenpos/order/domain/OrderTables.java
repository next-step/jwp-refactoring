package kitchenpos.order.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables;

    private static final int MINIMUM_TABLE_SIZE = 2;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    public List<OrderTable> findAll() {
        return this.orderTables;
    }

    public boolean sameSizeWith(int size) {
        return this.orderTables.size() == size;
    }

    public void updateTableGroup(TableGroup tableGroup) {
        orderTables.forEach(table -> table.updateTableGroup(tableGroup));
    }

    public void checkOrderStatus() {
        if (orderTables.stream().anyMatch(OrderTable::hasUnchangeableStatusOrder)) {
            throw new IllegalArgumentException("주문 상태가 조리중이거나 식사중인 테이블의 단체 지정은 해지할 수 없습니다.");
        }
    }

    public void clear() {
        this.orderTables.clear();
    }

    private void validate(List<OrderTable> orderTables) {
        checkTableSize(orderTables);
        for(OrderTable orderTable : orderTables) {
            checkEmptyOrAlreadyGroup(orderTable);
        }
    }

    private void checkTableSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException("2개 미만의 테이블은 단체 지정이 불가합니다.");
        }
    }

    private void checkEmptyOrAlreadyGroup(OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException("비어있지 않거나 이미 단체 지정된 테이블은 단체 지정이 불가합니다.");
        }
    }
}
