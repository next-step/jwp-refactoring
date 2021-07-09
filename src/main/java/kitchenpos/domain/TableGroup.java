package kitchenpos.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    @CreatedDate
    private LocalDateTime createdDate;

    public TableGroup() {
    }

    public TableGroup(final OrderTable ...orderTables) {
        this(null, orderTables);
    }

    public TableGroup(Long id, final OrderTable ...orderTables) {
        this.id = id;
        this.orderTables.append(this, orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.list();
    }

    public void changeEmpty(final boolean empty) {
        orderTables.changeEmpty(empty);
    }

    public void remove() {
        orderTables.clearTableGroup();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
