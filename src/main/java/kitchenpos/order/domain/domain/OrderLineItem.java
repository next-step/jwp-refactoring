package kitchenpos.order.domain.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.domain.Menu;
import kitchenpos.menu.exception.NotFoundMenuException;

@Entity
@Table(name = "order_line_item")
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_order_line_item_menu"), nullable = false)
    private Menu menu;

    @Embedded
    private Quantity quantity;

    protected OrderLineItem() {
    }

    private OrderLineItem(Long id, Menu menu, long quantity) {
        validate(menu);
        this.id = id;
        this.menu = menu;
        this.quantity = Quantity.of(quantity);
    }

    private void validate(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new NotFoundMenuException();
        }
    }

    public static OrderLineItem of(Menu menu, long quantity) {
        return of(null, menu, quantity);
    }

    public static OrderLineItem of(Long id, Menu menu, long quantity) {
        return new OrderLineItem(id, menu, quantity);
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }
}
