package kitchenpos.table.domain;

import kitchenpos.table.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
