package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    private LocalDateTime createdDate;

    protected TableGroup() {}

    private TableGroup(Long id) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
    }

    public static TableGroup from() {
        return new TableGroup(null);
    }

    public static TableGroup from(Long id) {
        return new TableGroup(id);
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::validateNotCompleteOrder);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
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
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
