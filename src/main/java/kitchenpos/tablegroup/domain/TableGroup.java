package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    public TableGroup() {
    }

    private TableGroup(Long id, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = LocalDateTime.now();
        this.orderTables = new OrderTables(orderTables, id);
    }

    private TableGroup(List<OrderTable> orderTables) {
        validSizeCheck(orderTables);
        this.createdDate = LocalDateTime.now();
        this.orderTables = new OrderTables(orderTables, id);
    }

    public static TableGroup of(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, orderTables);
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(orderTables);
    }

    private void validSizeCheck(List<OrderTable> orderTables) {
        validOrderTableEmpty(orderTables);
        validOrderTableMinSize(orderTables);
    }

    private void validOrderTableEmpty(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables)) {
            throw new IllegalArgumentException("주문 테이블이 입력되지 않았습니다.");
        }
    }

    private void validOrderTableMinSize(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new IllegalArgumentException("총 주문 테이블수는 2보다 작을 수 없습니다.");
        }
    }

    public void changeUnGroup() {
        this.orderTables.changeUnGroup();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }
}
