package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    //@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Long menuId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Embedded
    Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long productId, Quantity quantity) {
        this(0L, null, productId, quantity);
    }

    public MenuProduct(Long seq, Long menuId, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

//    public Price getTotalPrice() {
//        return productId.getPrice()
//                .multiply(BigDecimal.valueOf(quantity.value()));
//    }

    public void ofMenu(Menu menu) {
        this.menuId = menu.getId();
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

}
