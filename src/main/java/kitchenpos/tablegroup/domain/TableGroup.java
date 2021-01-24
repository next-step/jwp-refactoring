package kitchenpos.tablegroup.domain;

import kitchenpos.common.domain.BaseEntity;

import javax.persistence.*;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public TableGroup() {
    }

    public Long getId() {
        return id;
    }
}
