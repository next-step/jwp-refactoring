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

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {}

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

    public Long getId() {
        return id;
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
            throw new IllegalArgumentException();
        }
    }

    private static void duplicateorderCheck(List<Long> orderTableIds, OrderTables savedOrderTables) {
        List<OrderTable> orderTables = savedOrderTables.getOrderTables();
        if (orderTables.size() != orderTableIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    private static void underTwoRequestCheck(List<Long> orderTableIds) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables.getOrderTables());
    }

}
