package kitchenpos.ordertable.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.Order;
import org.springframework.util.CollectionUtils;

@Entity
public class TableGroup {

    private static final String ERROR_MESSAGE_ORDER_NOT_COMPLETE = "조리, 식사중인 주문이 존재하는 테이블이 있습니다.";
    private static final String ERROR_MESSAGE_NOT_ENOUGH_TABLES = "주문 테이블이 2개 이상일때 그룹화 가능 합니다.";
    private static final String ERROR_MESSAGE_NOT_EMPTY_TABLE = "그룹화를 위해선 테이블들이 주문종료 상태여야 합니다.";
    private static final String ERROR_MESSAGE_TABLE_ALREADY_IN_GROUP = "테이블 그룹에 이미 속해있는 주문테이블은 그룹화할 수 없습니다..";
    private static final String ERROR_MESSAGE_DUPLICATE_TALBES = "그룹대상 테이블에 중복이 존재합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public void groupTables(List<OrderTable> inputOrderTables) {
        validateGroupingCondition(inputOrderTables);

        orderTables.addAll(inputOrderTables);
        for (OrderTable orderTable : inputOrderTables) {
            orderTable.groupIn(this);
            orderTable.changeOrderClose(false);
        }
    }

    private void validateGroupingCondition(List<OrderTable> inputOrderTables) {
        validateAtLeastTwoDistinctTables(inputOrderTables);
        inputOrderTables.stream()
            .forEach(orderTable -> validateOrderTableStatus(orderTable));
    }

    private void validateNoDuplicateTable(List<OrderTable> inputOrderTables) {
        Set<OrderTable> distinctOrderTables = inputOrderTables.stream().collect(Collectors.toSet());

        if (inputOrderTables.size() != distinctOrderTables.size()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_DUPLICATE_TALBES);
        }
    }

    private void validateAtLeastTwoDistinctTables(List<OrderTable> inputOrderTables) {
        validateNoDuplicateTable(inputOrderTables);

        if (CollectionUtils.isEmpty(inputOrderTables) || inputOrderTables.size() < 2) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_ENOUGH_TABLES);
        }
    }

    private void validateOrderTableStatus(OrderTable orderTable) {
        validateEmptyTable(orderTable);
        validateNotInAnyGroup(orderTable);
    }

    private void validateNotInAnyGroup(OrderTable orderTable) {
        if (Objects.nonNull(orderTable.getTableGroup())) {
            throw new IllegalArgumentException(ERROR_MESSAGE_TABLE_ALREADY_IN_GROUP);
        }
    }

    private void validateEmptyTable(OrderTable orderTable) {
        if (!orderTable.isOrderClose()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NOT_EMPTY_TABLE);
        }
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            validateAllOrdersComplete(orderTable.getOrders());
            orderTable.unGroup();
        }
        orderTables.clear();
    }

    private void validateAllOrdersComplete(List<Order> orders) {
        long completeCount = orders.stream()
            .filter(Order::isCompleteStatus)
            .count();
        if (orders.size() != completeCount) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ORDER_NOT_COMPLETE);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TableGroup)) {
            return false;
        }

        TableGroup that = (TableGroup) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
