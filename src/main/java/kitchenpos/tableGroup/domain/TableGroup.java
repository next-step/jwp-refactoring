package kitchenpos.tableGroup.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.orderTable.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class TableGroup extends BaseEntity {
    public static final int MIN_ORDER_TABLE_COUNT = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(
            mappedBy = "tableGroup",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup(final Long id, final List<OrderTable> orderTables) {
        this.id = id;
        this.orderTables = orderTables;
    }

    public static TableGroup createTableGroup(final List<OrderTable> orderTables) {
        validate(orderTables);

        TableGroup tableGroup = new TableGroup();

        orderTables.forEach(tableGroup::addOrderTable);

        return tableGroup;
    }

    private void addOrderTable(final OrderTable orderTable) {
        orderTables.add(orderTable);
        orderTable.setTableGroup(this);
    }

    public void ungroup() {
        orderTables.forEach(orderTable -> orderTable.setTableGroup(null));
        orderTables = new ArrayList<>();
    }

    private static void validate(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_ORDER_TABLE_COUNT) {
            throw new IllegalArgumentException("단체 지정하려면 주문 테이블의 갯수가 최소 " + MIN_ORDER_TABLE_COUNT + "개 이상이여야 합니다.");
        }

        for (final OrderTable savedOrderTable : orderTables) {
            validateNonEmpty(savedOrderTable);
            validateExistTableGroup(savedOrderTable);
        }
    }

    private static void validateExistTableGroup(final OrderTable savedOrderTable) {
        if (Objects.nonNull(savedOrderTable.getTableGroup())) {
            throw new IllegalArgumentException("이미 단체지정된 주문 테이블이 존재합니다.");
        }
    }

    private static void validateNonEmpty(final OrderTable savedOrderTable) {
        if (!savedOrderTable.isEmpty()) {
            throw new IllegalArgumentException("비여있는 주문 테이블만 단체지정 가능합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
