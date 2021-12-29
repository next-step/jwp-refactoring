package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuPrice {

    @Column(name = "price")
    private long value;
    
    protected MenuPrice() {
    }
    
    private MenuPrice(long value) {
        validatePrice(value);
        this.value = value;
    }
    
    public static MenuPrice from(long value) {
        return new MenuPrice(value);
    }
    
    public long getValue() {
        return this.value;
    }
    
    public MenuPrice add(MenuPrice price) {
        return new MenuPrice(this.value + price.getValue());
    }
    
    public boolean isGreaterThan(MenuPrice targetPrice) {
        return this.value > targetPrice.getValue();
    }
    
    private void validatePrice(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("가격은 0원 이상이어야 합니다");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuPrice price = (MenuPrice) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
