package kitchenpos.ordertable.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderTables {
    private static final int MINIMUM_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {}

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validate(List<OrderTable> orderTables) {
        validateIsEmpty(orderTables);
        validateMinimumSize(orderTables);
    }

    private void validateIsEmpty(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("주문 테이블 목록에 주문 테이블이 없습니다.");
        }
    }

    private void validateMinimumSize(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_SIZE) {
            throw new IllegalArgumentException("주문 테이블은 2개 이상 존재해야 합니다.");
        }
    }

    public void validateGroup() {
        validateShouldEmpty();
        validateHasNotGroup();
    }

    private void validateShouldEmpty() {
        boolean hasNotEmpty = orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());

        if (hasNotEmpty) {
            throw new IllegalArgumentException("빈 상태가 아닌 주문테이블이 존재합니다.");
        }
    }

    private void validateHasNotGroup() {
        boolean hasGroup = orderTables.stream()
                .anyMatch(orderTable -> orderTable.getTableGroup() != null);

        if (hasGroup) {
            throw new IllegalArgumentException("이미 단체 지정된 주문 테이블이 존재합니다.");
        }
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> get() {
        return Collections.unmodifiableList(orderTables);
    }
}
