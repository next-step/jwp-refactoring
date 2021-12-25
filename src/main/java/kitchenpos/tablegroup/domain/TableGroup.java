package kitchenpos.tablegroup.domain;

import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
        validateTableGroup();
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
        validateTableGroup();
    }

    private void validateTableGroup() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        validateOrderTables();
    }

    private void validateOrderTables() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validateAddableOrderTable();
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

    public void ungroup() {
        for (OrderTable orderTable: orderTables) {
            orderTable.ungroupTableGroup();
        }
    }
}
