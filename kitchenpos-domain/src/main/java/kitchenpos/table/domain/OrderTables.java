package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "id")
    private List<OrderTable> orderTables = new ArrayList<>();

    public OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
    }

    /**
     * 그룹화 하려는 테이블이 없거나, 2개 이상인지 확인합니다.
     */
    public void validateOrderTablesSize() {
        if (CollectionUtils.isEmpty(this.orderTables) || this.orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 저장 된 테이블들이 유효한지(비어있지 않은지) 확인합니다.
     * @param savedOrderTables
     */
    public void comparedSavedOrderTables(List<OrderTable> savedOrderTables) {
        if (this.orderTables.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            if (!savedOrderTable.isEmpty() || Objects.nonNull(savedOrderTable.getTableGroupId())) {
                throw new IllegalArgumentException();
            }
        }
    }
}
