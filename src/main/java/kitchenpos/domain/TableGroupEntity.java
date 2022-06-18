package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "table_group")
public class TableGroupEntity {

    private static final int MIN_SIZE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime createdDate;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    public TableGroupEntity() {
        this.createdDate = LocalDateTime.now();
    }

    public void addOrderTables(List<OrderTableEntity> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables.addAll(this, orderTables);
    }

    private void validateOrderTables(List<OrderTableEntity> orderTables) {
        if (orderTables.size() < MIN_SIZE) {
            throw new InvalidOrderTablesException("테이블 갯수가 적습니다.");
        }
        if (hasNotEmptyOrGrouped(orderTables)) {
            throw new InvalidOrderTablesException(
                    "테이블 중에 빈 테이블이 아니거나 다른 단체에 지정된 테이블이 있습니다.");
        }
    }

    private boolean hasNotEmptyOrGrouped(List<OrderTableEntity> orderTables) {
        return orderTables.stream().anyMatch(it -> !it.isEmpty() || it.isGrouped());
    }

    public void ungroup() {
        orderTables.clear();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableEntity> getOrderTables() {
        return orderTables.get();
    }
}
