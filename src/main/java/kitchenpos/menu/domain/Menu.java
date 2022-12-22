package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {

    private static final String INVALID_PRICE = "유요하지 않은 가격입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private BigDecimal price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {

    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        validatePriceValue(price);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    private void validatePriceValue(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(INVALID_PRICE);
        }
    }

    private void validateMenuPrice() {
        if (price.compareTo(menuProducts.getMenuProductPriceSum()) > 0) {
            throw new IllegalArgumentException(INVALID_PRICE);
        }
    }

    public void addMenuProducts(List<MenuProduct> menuProductList) {
        menuProducts.addList(menuProductList);
        validateMenuPrice();
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
        return menuProducts.getMenuProducts();
    }

}
