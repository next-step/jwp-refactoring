package kitchenpos.domain;

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

@Entity
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_order_product_order_menu_product"))
    private OrderMenuProduct orderMenuProduct;

    // protected OrderProduct() {
    // }
    //
    // public OrderProduct(String name, Price price) {
    //     this(null, name, price);
    // }
    //
    // OrderProduct(Long id, String name, Price price) {
    //     checkArguments(name, price);
    //     this.id = id;
    //     this.name = name;
    //     this.price = price;
    // }
    //
    // private void checkArguments(String name, Price price) {
    //     if (Objects.isNull(name) || Objects.isNull(price)) {
    //         throw new IllegalArgumentException("제품을 생성하려면 모든 필수값이 입력되어야 합니다.");
    //     }
    // }
    //
    // public Price priceOf(Quantity quantity) {
    //     return price.of(quantity);
    // }
    //
    // public Long getId() {
    //     return id;
    // }
    //
    // public String getName() {
    //     return name;
    // }
    //
    // public Price getPrice() {
    //     return price;
    // }
}
