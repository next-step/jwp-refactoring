package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    private TableGroup(TableGroupBuilder builder) {
        this.id = builder.id;
        this.createdDate = builder.createdDate;
        this.orderTables = builder.orderTables;
    }

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
        boolean isUse = orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId()));
        if(isUse){
            throw new IllegalArgumentException();
        }
        this.orderTables.addAll(orderTables);
    }

    public void ungroup(List<Order> orders) {
        boolean isEnable = orders.stream()
                .anyMatch(order -> order.getOrderStatus().equals(OrderStatus.COOKING.name()) || order.getOrderStatus().equals(OrderStatus.MEAL.name()));
        if(isEnable){
            throw new IllegalArgumentException();
        }
        orderTables.stream().forEach(orderTable -> orderTable.setTableGroup(null));
    }

    public static class TableGroupBuilder {
        private Long id;
        private LocalDateTime createdDate;
        private List<OrderTable> orderTables = new ArrayList<>();

        public TableGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TableGroupBuilder createDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public TableGroupBuilder orderTables(List<OrderTable> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
    public List<Long> getOrderTableIds() {
        return orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
    }


    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
