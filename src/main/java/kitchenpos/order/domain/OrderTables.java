package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static kitchenpos.common.constants.ErrorCodeType.*;

@Embeddable
public class OrderTables {

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validCheckOrderTableIsEmptyAndMinSize(orderTables);
        this.orderTables = new ArrayList<>(orderTables);
    }

    private void validCheckOrderTableIsEmptyAndMinSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException(ORDER_TABLES_SIZE_IS_EMPTY_AND_MIN_SIZE.getMessage());
        }
    }

    private void validHasGroup() {
        boolean matchGroup = orderTables.stream()
                .anyMatch(OrderTable::isNotNull);

        if (matchGroup) {
            throw new IllegalArgumentException(MATCH_GROUP_PRESENT.getMessage());
        }
    }

    private void validIsNotEmpty() {
        boolean matchIsNotEmpty = orderTables.stream()
                .anyMatch(orderTable -> !orderTable.isEmpty());

        if (matchIsNotEmpty) {
            throw new IllegalArgumentException(NOT_MATCH_ORDER_TABLE.getMessage());
        }
    }


    public void validCheckTableGroup() {
        validIsNotEmpty();
        validHasGroup();
    }
}
