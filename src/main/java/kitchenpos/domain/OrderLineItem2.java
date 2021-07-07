package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_line_item") // TODO 변경하고 삭제
public class OrderLineItem2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order2 order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private long quantity;

    protected OrderLineItem2() {
    }
}
