package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menu.domain.Amount;

@Entity
public class Product {
    private static final int FREE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer price;

    protected Product() {
    }

    public Product(String name, Integer price) {
        validatePrice(price);
        this.name = name;
        this.price = price;
    }

    public Product(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePrice(Integer price) {
        if (price == null || price < FREE) {
            throw new IllegalArgumentException("[ERROR] 상품 가격은 0원 이상 이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Amount createAmount(int quantity) {
        return new Amount(price, quantity);
    }
}
