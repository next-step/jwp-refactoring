package kitchenpos.menu.domain.embedded;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuPrice {

    @Column(nullable = false)
    private BigDecimal price;

    protected MenuPrice(){

    }

    public MenuPrice(BigDecimal price) {
        this.price = price;
        validateCreate();
    }

    private void validateCreate() {
        if(isNull() || isLessThan(BigDecimal.ZERO)){
            throw new IllegalArgumentException();
        }
    }

    public MenuPrice(int price){
        this.price = new BigDecimal(price);
    }

    private boolean isNull(){
        return Objects.isNull(price);
    }

    public boolean isLessThan(BigDecimal target) {
        return price.compareTo(target) < 0;
    }

    public BigDecimal multiply(long operation){
        return price.multiply(BigDecimal.valueOf(operation));
    }

    public BigDecimal getPrice() {
        return price;
    }
}
