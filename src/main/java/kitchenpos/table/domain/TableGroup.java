package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.exception.InvalidStatusException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    private static final int MIN_TABLES_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private OrderTables orderTables;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    protected TableGroup() {
    }

    private TableGroup(OrderTables orderTables) {
        validate(orderTables);
        orderTables.changeGroup(this);
        this.orderTables = orderTables;
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(OrderTables.from(orderTables));
    }

    public long id() {
        return id;
    }

    public LocalDateTime createdDate() {
        return createdDate;
    }

    public List<OrderTable> orderTables() {
        return orderTables.list();
    }

    public void ungroup() {
        if (orderTables.anyOrdered()) {
            throw new InvalidStatusException(
                String.format("조리중 또는 식사중인 주문 테이블들(%s)이 존재하여 단체 지정을 해제할 수 없습니다.", orderTables));
        }
        orderTables.ungroup();
    }

    private void validate(OrderTables orderTables) {
        Assert.notNull(orderTables, "주문 테이블들은 필수입니다.");
        Assert.isTrue(orderTables.size() >= MIN_TABLES_SIZE, String
            .format("단체 지정하려는 주문 테이블들(%s)은 적어도 %d개 이상 이어야 합니다.", orderTables, MIN_TABLES_SIZE));
        Assert.isTrue(orderTables.notHaveGroupAndEmpty(),
            String.format("단체 지정하려는 주문 테이블들(%s)은 그룹이 없어나 비어있어야 합니다.", orderTables));
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderTables);
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
        return Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public String toString() {
        return "TableGroup{" +
            "id=" + id +
            ", orderTables=" + orderTables +
            ", createdDate=" + createdDate +
            '}';
    }
}
