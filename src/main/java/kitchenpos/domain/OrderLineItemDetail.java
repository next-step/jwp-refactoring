package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderLineItemDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @Column(nullable = false)
    private String name;

    @Embedded
    @Column(nullable = false)
    private Price price;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    // protected OrderMenuProduct() {
    // }
    //
    // public OrderMenuProduct(Product product, Quantity quantity) {
    //     this(null, product, quantity);
    // }
    //
    // private OrderMenuProduct(Menu menu, Product product, Quantity quantity) {
    //     this.menu = menu;
    //     this.product = product;
    //     this.quantity = quantity;
    // }
    //
    // public OrderMenuProduct withMenu(Menu menu) {
    //     checkAllocation();
    //     this.menu = menu;
    //     return this;
    // }
    //
    // private void checkAllocation() {
    //     if (Objects.nonNull(this.menu)) {
    //         throw new AlreadyAllocatedException("매핑 정보의 메뉴를 재 할당 할 수 없습니다.");
    //     }
    // }
    //
    // public Price getTotalPrice() {
    //     return product.priceOf(quantity);
    // }
    //
    // public Long getSeq() {
    //     return seq;
    // }
    //
    // public Menu getMenu() {
    //     return menu;
    // }
    //
    // public Product getProduct() {
    //     return product;
    // }
    //
    // public Quantity getQuantity() {
    //     return quantity;
    // }
}
