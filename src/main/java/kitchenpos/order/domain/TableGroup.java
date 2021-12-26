package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    private TableGroup(List<OrderTable> orderTables) {
        validateSize(orderTables);
        validateEmptyOrderTables(orderTables);
        validateTableGroup(orderTables);
        this.createdDate = LocalDateTime.now();
        this.orderTables = orderTables;
        group(orderTables);
    }

    public static TableGroup from(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
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

    public void group(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.group(this));
    }

    private void validateSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("단체 지정 시 주문테이블은 두 테이블 이상 이어야 합니다.");
        }
    }

    private void validateTableGroup(List<OrderTable> orderTables) {
        boolean isGrouped = orderTables.stream()
                .anyMatch(OrderTable::isGrouped);
        if (isGrouped) {
            throw new IllegalArgumentException("이미 주문 테이블이 단체 지정이 되어있으므로 지정할 수 없습니다.");
        }
    }

    private void validateEmptyOrderTables(List<OrderTable> orderTables) {
        boolean notEmpty = orderTables.stream().anyMatch(OrderTable::isNotEmpty);
        if (notEmpty) {
            throw new IllegalArgumentException("주문 테이블이 빈 테이블이 아니면 등록할 수 없습니다.");
        }
    }
}
