package kitchenpos.domain;

import static javax.persistence.GenerationType.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TableGroup extends BaseEntity {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    public TableGroup() {
    }

    public Long getId() {
        return id;
    }

}
