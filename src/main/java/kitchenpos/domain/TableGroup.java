package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private final List<OrderTable> orderTables = new ArrayList<>();

    public static TableGroup createTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        validate(orderTables);
        TableGroup tableGroup = new TableGroup(createdDate);
        tableGroup.orderTables.addAll(orderTables);
        return tableGroup;
    }

    private TableGroup(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    protected TableGroup() {
    }

    public void clearTables() {
        orderTables.forEach(OrderTable::releaseInGroup);
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

    private static void validate(List<OrderTable> orderTables) {
        if (isNotValidOrderTables(orderTables)) {
            throw new IllegalArgumentException("주문 그룹으로 설정될 주문 테이블은 Group으로 설정되지 않거나, 점유되어있지 않아야합니다.");
        }
    }

    private static boolean isNotValidOrderTables(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> orderTable.isNotEmpty() || orderTable.hasTableGroup());
    }
}
