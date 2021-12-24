package kitchenpos.order.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.util.List;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

import kitchenpos.common.exception.BadRequestException;

@Embeddable
public class OrderTables {

    public static final int ORDER_TABLE_MIN_SIZE = 2;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected OrderTables() {
    }

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private void validate(List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateOrderTable(orderTables);
    }

    private void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < ORDER_TABLE_MIN_SIZE) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    private void validateOrderTable(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            validatePossibleIntoTableGroup(orderTable);
        }
    }

    private void validatePossibleIntoTableGroup(OrderTable orderTable) {
        if (!orderTable.isPossibleIntoTableGroup()) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    public void validateNotCompletionOrderStatus() {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateNotCompletionOrderStatus();
        }
    }

    public List<OrderTable> getValue() {
        return orderTables;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        for (OrderTable orderTable : orderTables) {
            // orderTable.changeTableGroup(tableGroup);
        }
    }

    public void ungroup() {
        for (OrderTable orderTable : orderTables) {
            orderTable.deleteTableGroup();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderTables that = (OrderTables)o;
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
    }
}
