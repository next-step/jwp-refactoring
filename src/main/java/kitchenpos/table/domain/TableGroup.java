package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.ExistGroupTableException;
import kitchenpos.exception.InvalidTableNumberException;
import kitchenpos.exception.NotEmptyException;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "table_group")
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {
    }

    public TableGroup(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new InvalidTableNumberException();
        }
        for (OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.existTableGroup()) {
            throw new ExistGroupTableException();
        }
        if (!orderTable.isEmpty()) {
            throw new NotEmptyException();
        }
    }

    public TableGroup(LocalDateTime createdDate) {
        this(null, createdDate, null);
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, null, orderTables);
    }

    public void addOrderTable(OrderTable orderTable) {
        if (this.orderTables == null) {
            this.orderTables = new ArrayList<>();
        }
        this.orderTables.add(orderTable);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void ungroup() {
        this.orderTables.forEach(OrderTable::ungroupTable);
    }

    public TableGroupResponse toTableGroupResponse() {
        final List<OrderTableResponse> orderTableResponses = this.orderTables.stream()
                .map(OrderTable::toOrderTableResponse)
                .collect(Collectors.toList());
        return new TableGroupResponse(this.id, this.createdDate, orderTableResponses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
