package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {

    private static final int FREE = 0;

    @Column(nullable = false)
    private int price;

    protected ProductPrice() {
    }

    public ProductPrice(Integer price) {
        validate(price);
        this.price = price;
    }

    private void validate(Integer price) {
        if (price == null || price < FREE) {
            throw new IllegalArgumentException("[ERROR] 상품 가격은 0원 이상 이어야 합니다.");
        }
    }

    public int getPrice() {
        return price;
    }
}
