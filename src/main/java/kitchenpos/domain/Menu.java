package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
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
    @Embedded
    private MenuName name;
    @Embedded
    private Price price;
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(MenuName name, Price price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, MenuName name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(Long id, MenuName name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        validateSumPrice(this.menuProducts);
        this.menuProducts.addMenu(this);
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = MenuProducts.from(menuProducts);
        validateSumPrice(this.menuProducts);
        this.menuProducts.addMenu(this);
    }

    private void validateSumPrice(MenuProducts menuProducts) {
        if (price.compareTo(menuProducts.sumOfPrice()) > 0) {
            throw new IllegalArgumentException("메뉴 상품 가격의 총 합은 메뉴 가격보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public MenuName getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}
