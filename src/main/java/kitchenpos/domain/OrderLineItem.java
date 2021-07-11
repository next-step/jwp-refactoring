package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;

@Entity
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String name;

    private Price price;

    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_order_line_item_menu"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_line_item_seq", foreignKey = @ForeignKey(name = "fk_order_menu_product_order_line_item"))
    private List<OrderLineItemDetail> orderLineItemDetails;

    protected OrderLineItem() {
    }

    public OrderLineItem(Menu menu, String name, Price price, Quantity quantity) {
        this(null, menu, name, price, quantity);
    }

    OrderLineItem(Long seq, Menu menu, String name, Price price, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderLineItem that = (OrderLineItem)o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
