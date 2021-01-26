package kitchenpos.ordertablegroup.domain;

import kitchenpos.common.domain.BaseEntity;

import javax.persistence.*;

@Entity(name = "table_group")
public class OrderTableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public OrderTableGroup() {
    }

    public Long getId() {
        return id;
    }
}
