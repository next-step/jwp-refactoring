package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

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

    private void validatePrice(Integer price) {
        if(price == null || price < 0){
            throw new IllegalArgumentException("[ERROR] 상품 가격은 0원 이상 이어야 합니다.");
        }
    }
}
