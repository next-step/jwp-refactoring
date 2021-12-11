package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class TableGroup {

    private static final int MIN_TABLES_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    public TableGroup() {
    }

    private TableGroup(OrderTables orderTables) {
        Assert.notNull(orderTables, "주문 테이블들은 필수입니다.");
        Assert.isTrue(orderTables.hasSizeMoreThan(MIN_TABLES_SIZE), String
            .format("단체 지정하려는 주문 테이블들(%s)은 적어도 %d개 이상 이어야 합니다.", orderTables, MIN_TABLES_SIZE));
        this.orderTables = orderTables;
    }

    public static TableGroup from(OrderTables orderTables) {
        return new TableGroup(orderTables);
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

    public OrderTables getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final OrderTables orderTables) {
        this.orderTables = orderTables;
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
