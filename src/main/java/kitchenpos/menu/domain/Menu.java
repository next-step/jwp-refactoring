package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Price;
import kitchenpos.menugroup.domain.MenuGroup;


@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(Long id, String name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(String name, Price price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
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

    public Price getPrice() {
        return price;
    }

    public void setPrice(final Price price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(MenuProducts menuProducts) {
        validatePrice(menuProducts.totalMenuPrice());
        this.menuProducts = menuProducts;
        menuProducts.getMenuProducts().forEach(menuProduct -> menuProduct.setMenu(this));
    }

    private void validatePrice(Price totalPrice) {
        if (price.isBiggerThan(totalPrice)) {
            throw new IllegalArgumentException("메뉴의 가격이 전체 메뉴 상품 가격의 합보다 클 수 없습니다.");
        }
    }
}
