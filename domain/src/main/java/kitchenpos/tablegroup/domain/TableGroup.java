package kitchenpos.tablegroup.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "table_group")
public class TableGroup {

    public TableGroup() {
        this.createdDate = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
