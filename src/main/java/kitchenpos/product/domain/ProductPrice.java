package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductPrice {
    @Column(nullable = false)
    BigDecimal price;

    ProductPrice(){

    }
    public ProductPrice(int price) {
        this(new BigDecimal(price));
    }

    public ProductPrice(double price) {
        this(new BigDecimal(price));
    }

    public ProductPrice(BigDecimal price) {
        if(Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("상품의 가격이 유효하지 않습니다.");
        }
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductPrice that = (ProductPrice) o;
        return Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
