package kitchenpos.table.domain;

import kitchenpos.common.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected TableGroup() {
    }

    private TableGroup(TableGroupBuilder builder) {
        this.id = builder.id;
    }

    public Long getId() {
        return id;
    }

    public static class TableGroupBuilder {
        private Long id;

        public TableGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(this);
        }
    }

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }
}
