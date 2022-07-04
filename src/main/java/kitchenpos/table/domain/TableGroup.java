package kitchenpos.table.domain;

import static kitchenpos.table.exception.CannotMakeTableGroupException.INSUFFICIENT_NUMBER_OF_TABLE;
import static kitchenpos.table.exception.CannotMakeTableGroupException.NOT_EXIST_TABLE;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.table.exception.CannotMakeTableGroupException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {
    @Embedded
    private final OrderTables orderTables = new OrderTables();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    protected TableGroup() {

    }

    public TableGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new CannotMakeTableGroupException(NOT_EXIST_TABLE);
        }
        if (orderTables.size() < 2) {
            throw new CannotMakeTableGroupException(INSUFFICIENT_NUMBER_OF_TABLE);
        }
        this.orderTables.addOrderTables(orderTables);
        this.orderTables.includeToTableGroup(this);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.toList();
    }

    public void ungroup() {
        orderTables.ungroup();
    }


}
