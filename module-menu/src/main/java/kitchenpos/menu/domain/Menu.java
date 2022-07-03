package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    private Menu(Long id) {
        this.id = id;
    }

    public Menu(String name, Integer price, Long menuGroupId,
                List<MenuProduct> menuProducts) {
        this.name = Name.of(name);
        this.price = Price.from(price);
        this.menuGroup = MenuGroup.of(menuGroupId);
        this.menuProducts = MenuProducts.of(this, menuProducts);
    }

    public static Menu of(Long id) {
        return new Menu(id);
    }

    public static Menu of(String name, Integer price, Long menuGroupId,
                          List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void validateMenuPrice(Price menuPrice) {
        if (menuPrice.isGreaterThan((price))) {
            throw new IllegalArgumentException("상품들 금액의 합이 메뉴 가격보다 클 수 없습니다");
        }
    }
}
