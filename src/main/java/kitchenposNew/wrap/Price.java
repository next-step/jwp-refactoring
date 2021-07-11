package kitchenposNew.wrap;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Price {
    private final BigDecimal price;

    private Price(){
        this.price = BigDecimal.ZERO;
    }

    public Price(BigDecimal price){
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal calculateTotalPrice(Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(price, price1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public boolean isCheapThanProductsPrice(BigDecimal productsPrice) {
        return price.compareTo(productsPrice) <= 0;
    }
}
