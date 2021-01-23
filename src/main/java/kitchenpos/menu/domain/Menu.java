package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import kitchenpos.domain.MenuProduct;

public class Menu {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(String name, BigDecimal price, long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static class Builder {
        private String name;
        private BigDecimal price;
        private Long menuGroupId;
        private List<MenuProduct> menuProducts;

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
        public Builder menuProducts(List<MenuProduct> menuProducts){
            this.menuProducts = menuProducts;
            return this;
        }

        public Menu build() {
            Menu menu = new Menu();
            menu.name = this.name;
            menu.price = this.price;
            menu.menuGroupId = this.menuGroupId;
            menu.menuProducts = this.menuProducts;
            return menu;
        }

    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
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
