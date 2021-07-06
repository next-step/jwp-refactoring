package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.util.CollectionUtils;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.tablegroup.exception.AlreadyUseTableException;
import kitchenpos.tablegroup.exception.NoTableSizeException;
import kitchenpos.tablegroup.exception.NotAbaliableOrderTableException;

@Entity
public class TableGroup {
    public static final TableGroup EMPTY = new TableGroup();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {}

    public TableGroup(Long id, LocalDateTime localDateTime, OrderTables orderTables) {
        this.id = id;
        this.createdDate = localDateTime;
        this.orderTables = orderTables;
    }

    public TableGroup(Long id, LocalDateTime localDateTime) {
        this(id, localDateTime, null);
    }

    public TableGroup(LocalDateTime localDateTime, OrderTables orderTables) {
        this(null, localDateTime, orderTables);
    }

    public static TableGroup make(List<Long> orderTableIds, OrderTables savedOrderTables) {
        validataion(orderTableIds, savedOrderTables);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        savedOrderTables.chargedBy(tableGroup);

        return tableGroup;
    }

    private static void validataion(List<Long> orderTableIds, OrderTables savedOrderTables) {
        underTwoRequestCheck(orderTableIds);
        duplicateorderCheck(orderTableIds, savedOrderTables);
        avaliableTableCheck(savedOrderTables);
    }

    private static void avaliableTableCheck(OrderTables savedOrderTables) {
        if (!savedOrderTables.avaliableTable()) {
            throw new AlreadyUseTableException();
        }
    }

    private static void duplicateorderCheck(List<Long> orderTableIds, OrderTables savedOrderTables) {
        List<OrderTable> orderTables = savedOrderTables.getOrderTables();
        if (orderTables.size() != orderTableIds.size()) {
            throw new NotAbaliableOrderTableException();
        }
    }

    private static void underTwoRequestCheck(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new NoTableSizeException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables.getOrderTables());
    }

    public Long getId() {
        return id;
    }

    public void ungroup() {
        orderTables.ungroup();
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

}
