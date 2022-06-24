package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "table_group")
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        addOrderTables(orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        addOrderTables(orderTables);
    }

    public void addOrderTables(final List<OrderTable> orderTables) {
        validateAddingOrderTables(orderTables);
        addAll(orderTables);
    }

    private void addAll(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            add(orderTable);
        }
    }

    private void add(OrderTable orderTable) {
        if (!orderTables.contains(orderTable)) {
            orderTables.add(orderTable);
        }
        orderTable.setTableGroup(this);
    }

    private void validateAddingOrderTables(final List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        if (validateOrderTableEmptyOrInTableGroup(orderTables)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean validateOrderTableEmptyOrInTableGroup(final List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(orderTable -> !orderTable.isEmpty() || orderTable.isInTableGroup());
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
}
