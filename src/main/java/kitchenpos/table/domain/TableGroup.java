package kitchenpos.table.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    public static TableGroup of() {
        return new TableGroup();
    }

    public TableGroup(List<OrderTable> orderTables) {
        validateTableGroup(orderTables);

        this.orderTables.addOrderTables(orderTables);
        this.orderTables.includeToTableGroup(this);
    }

    private void validateTableGroup(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("테이블이 리스트가 없으면 테이블 그룹을 등록할 수 없습니다.");
        }
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블이 2개 미만인 경우 테이블 그룹을 등록할 수 없습니다.");
        }
    }

    public void ungroup() {
        orderTables.ungroup();
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
}
