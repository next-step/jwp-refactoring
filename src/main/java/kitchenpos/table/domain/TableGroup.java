package kitchenpos.table.domain;

import kitchenpos.common.BaseEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public TableGroup() {}

    public TableGroup(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
