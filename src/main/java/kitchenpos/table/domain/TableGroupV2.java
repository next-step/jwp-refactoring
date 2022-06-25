package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class TableGroupV2 {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany
    private List<OrderTableV2> orderTables = new ArrayList<>();

    protected TableGroupV2() {
    }

    public TableGroupV2(Long id, LocalDateTime createdDate, List<OrderTableV2> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroupV2 that = (TableGroupV2) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
