package kitchenpos.domain.table;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "id")
    private List<OrderTable> orderTables;

    protected TableGroup() {
    }

    private TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(null, LocalDateTime.now(), orderTables);
    }

    public static TableGroup of(Long id, List<OrderTable> orderTables) {
        return new TableGroup(id, LocalDateTime.now(), orderTables);
    }

    public Long getId() {
        return this.id;
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return this.orderTables;
    }

    public void changeCreatedDate(LocalDateTime localDateTime) {
        this.createdDate = localDateTime;
    }

    public void changeOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }
}
