package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<OrderTable> orderTables;

    public TableGroup() {
        orderTables = new HashSet<>();
    }

    public TableGroup(Collection<OrderTable> orderTables) {
        this.orderTables = new HashSet<>(orderTables);
    }

    public TableGroup(Long id, LocalDateTime createdDate, Collection<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = new HashSet<>(orderTables);
        orderTables.forEach(orderTable -> orderTable.group(this));
    }

    public void add(OrderTable orderTable) {
        this.orderTables.add(orderTable);
        orderTable.group(this);
    }

    public void ungroup() {
        orderTables.forEach(OrderTable::ungroup);
        orderTables.clear();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return new ArrayList<>(orderTables);
    }
}
