package kitchenpos.table.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.common.exception.InvalidParameterException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderTables {
    private static final String ERROR_MESSAGE_ORDER_TABLES_IS_EMPTY = "주문 테이블은 비어있을 수 없습니다.";
    private static final String ERROR_MESSAGE_ORDER_TABLES_MIN_SIZE = "주문 테이블은 2석 이상이어야 합니다.";

    private static final int TABLES_MIN_SIZE = 2;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "table_group_id")
    private List<OrderTable> tables = new ArrayList<>();

    protected OrderTables() {}

    private OrderTables(List<OrderTable> tables) {
        validate(tables);
        this.tables = tables;
    }

    private void validate(List<OrderTable> tables) {
        validateTableIsEmpty(tables);
        validateTableSize(tables);
        validateIsGrouped(tables);
    }

    private void validateTableIsEmpty(List<OrderTable> tables) {
        if (CollectionUtils.isEmpty(tables)) {
            throw new InvalidParameterException(ERROR_MESSAGE_ORDER_TABLES_IS_EMPTY);
        }
    }

    private void validateTableSize(List<OrderTable> tables) {
        if (tables.size() < TABLES_MIN_SIZE) {
            throw new InvalidParameterException(ERROR_MESSAGE_ORDER_TABLES_MIN_SIZE);
        }
    }

    private void validateIsGrouped(List<OrderTable> tables) {
        tables.forEach(OrderTable::validateGrouped);
    }

    public static OrderTables from(List<OrderTable> tables) {
        return new OrderTables(tables);
    }

    public boolean validNotEmptyOrderTable() {
        return !tables.stream().allMatch(OrderTable::isEmpty);
    }

    public void validAlreadyGroup() {
        tables.forEach(OrderTable::validateGrouped);
    }

    public void updateTableGroup(TableGroup tableGroup) {
        tables.forEach(orderTable -> orderTable.updateTableGroup(tableGroup.id()));
    }

    public void ungroup() {
        tables.forEach(OrderTable::ungroup);
    }

    public List<OrderTable> list() {
        return Collections.unmodifiableList(tables);
    }
}
