package kitchenpos.core.domain;

import kitchenpos.core.exception.IllegalOrderTableIdsException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class TableGroupOrderTableIds {
    private static final String MIN_ORDER_TABLE_SIZE_ERROR_MESSAGE = "2개 이상의 주문 테이블로 구성되어야 한다.";
    private static final int MIN_SIZE = 2;

    @ElementCollection
    @CollectionTable(name = "table_group_order_table",
            joinColumns = @JoinColumn(
                    name = "table_group_id",
                    foreignKey = @ForeignKey(name = "fk_table_group_order_table"),
                    nullable = false, updatable = false))
    @Column(nullable = false, name = "order_table_id")
    private final List<Long> orderTableIds = new ArrayList<>();

    protected TableGroupOrderTableIds() {
    }

    private TableGroupOrderTableIds(List<Long> orderTableIds) {
        validate(orderTableIds);
        this.orderTableIds.addAll(orderTableIds);
    }

    private void validate(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < MIN_SIZE) {
            throw new IllegalOrderTableIdsException(MIN_ORDER_TABLE_SIZE_ERROR_MESSAGE);
        }
    }

    public static TableGroupOrderTableIds of(List<Long> orderTableIds) {
        return new TableGroupOrderTableIds(orderTableIds);
    }

    public List<Long> getOrderTableIds() {
        return Collections.unmodifiableList(orderTableIds);
    }
}
