package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Table(name = "order_line_item")
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 양방향
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne // 단방향
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "quantity")
    private long quantity;

    protected OrderLineItem() {
    }

    public void addedBy(final Order order) {
        this.order = order;
    }
}
