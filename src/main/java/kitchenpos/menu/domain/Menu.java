package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menu.exception.MenuPriceCannotBeNegativeException;
import kitchenpos.menu.exception.MenuPriceEmptyException;
import kitchenpos.product.domain.Product;

@Entity
public class Menu {

    public static final String THE_SUM_OF_THE_PRICES_OF_THE_GOODS_HAS_EXCEEDED = "메뉴의 가격은 메뉴상품들 가격의 합을 초과할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(Long id) {
        this.id = id;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, new MenuGroup(menuGroupId), menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validationPrice(price, menuProducts);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.toMenu(this);
        }
    }

    private void validationPrice(BigDecimal price, List<MenuProduct> menuProducts) {
        if (Objects.isNull(price)) {
            throw new MenuPriceEmptyException();
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new MenuPriceCannotBeNegativeException();
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice()
                .multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        if (price.compareTo(sum) > 0) {
            throw new InvalidMenuPriceException(THE_SUM_OF_THE_PRICES_OF_THE_GOODS_HAS_EXCEEDED);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

}
