package kitchenpos.table.domain;

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
import kitchenpos.table.exception.CannotMakeTableGroupException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup", cascade = {CascadeType.MERGE})
    private List<OrderTable> orderTables = new ArrayList<>();

    protected TableGroup() {

    }

    public TableGroup(List<OrderTable> savedOrderTables) {
        if (CollectionUtils.isEmpty(savedOrderTables)) {
            throw new CannotMakeTableGroupException("there is no saved table");
        }
        if (savedOrderTables.size() < 2) {
            throw new CannotMakeTableGroupException("number of tables in table group should larger than 1");
        }
        this.orderTables.addAll(savedOrderTables);
        this.orderTables.forEach(orderTable -> orderTable.includeTo(this));
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

    public void ungroup(){
        orderTables.forEach(orderTable -> orderTable.ungroup());
    }
}
