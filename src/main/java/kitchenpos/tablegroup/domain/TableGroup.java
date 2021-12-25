package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kitchenpos.ordertable.domain.OrderTable;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20)")
    private Long id;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<OrderTable> orderTables;

    public TableGroup() {
    }

    public TableGroup(Long id) {
        this.id = id;
    }

    public TableGroup(List<OrderTable> orderTables) {
        this(null, orderTables);
    }

    public TableGroup(Long id, List<OrderTable> orderTables) {
        groupOrderTable(orderTables);
        this.id = id;
        this.orderTables = orderTables;
    }

    private void groupOrderTable(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.toGroup(this));
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
}
