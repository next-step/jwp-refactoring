package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(String name, BigDecimal price, long menuGroupId, MenuProducts menuProducts) {
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(name, price, menuGroupId, null);
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts = new MenuProducts();
        this.menuProducts.add(menuProduct);
    }

    public void addMenuProducts(MenuProducts menuProducts) {
        menuProducts.validateSumOfPrice(price);
        menuProducts.getMenuProducts().forEach(menuProduct -> {
            addMenuProduct(new MenuProduct(this, menuProduct.getProduct(), menuProduct.getQuantity()));
        });
    }

    public static class Builder {
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private MenuProducts menuProducts;

        public Builder() {
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price){
            this.price = price;
            return this;
        }
        public Builder menuGroupId(Long menuGroupId){
            this.menuGroupId = menuGroupId;
            return this;
        }
        public Builder menuProducts(MenuProducts menuProducts){
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            return new Menu(name, price, menuGroupId, menuProducts);
        }

    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Menu menu = (Menu)o;
        return Objects.equals(getId(), menu.getId()) && Objects.equals(getName(), menu.getName())
            && Objects.equals(getPrice(), menu.getPrice()) && Objects.equals(getMenuGroupId(),
            menu.getMenuGroupId()) && Objects.equals(getMenuProducts(), menu.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getMenuGroupId(), getMenuProducts());
    }

    @Override
    public String toString() {
        return "Menu{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", menuGroupId=" + menuGroupId +
            ", menuProducts=" + menuProducts +
            '}';
    }
}
