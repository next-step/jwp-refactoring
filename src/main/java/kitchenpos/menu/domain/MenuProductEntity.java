package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "menu_product")
public class MenuProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuEntity menu;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private Long quantity;

    protected MenuProductEntity() {
    }

    public MenuProductEntity(MenuEntity menu, ProductEntity product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProductEntity(ProductEntity productEntity, long quantity) {
        this.product = productEntity;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public MenuEntity getMenu() {
        return menu;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getMenuProductPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void updateMenu(MenuEntity menuEntity) {
        this.menu = menuEntity;
    }
}
