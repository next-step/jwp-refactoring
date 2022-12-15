package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.common.Price;
import kitchenpos.common.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_to_menu"))
    private Menu menu;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_to_product"))
    private Product product;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = Quantity.of(quantity);
    }
    public static MenuProduct of(Product product, int quantity){
        return new MenuProduct(product, quantity);
    }

    public Price getTotalPrice(){
        return product.getPrice().multiply(getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void updateMenu(Menu menu) {
        this.menu = menu;
    }


}
