package kitchenpos.application.menu;

import kitchenpos.application.menugroup.MenuGroup;
import kitchenpos.application.valueobject.Price;

import javax.persistence.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        menuProducts.updateMenu(this);
        validate(price, menuProducts);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validate(Price price, MenuProducts menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("등록된 상품으로만 메뉴를 등록할 수 있습니다.");
        }
        if (price.isBiggerThan(menuProducts.calculateSumPrice())) {
            throw new IllegalArgumentException("메뉴의 가격이 메뉴에 속한 상품 금액의 합보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}