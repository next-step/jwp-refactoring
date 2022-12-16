package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "menu")
public class Menu {
    private static final String EXCEPTION_MESSAGE_MENU_PRICE_INVALID = "메뉴의 가격은 상품의 가격 총합보다 작거나 같아야 합니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, columnDefinition = "bigint(20)")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Embedded
    private Price price;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "menu_group_id", nullable = false, columnDefinition = "bigint(20)", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;
    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;

        for (MenuProduct menuProduct : menuProducts) {
            validateMenuPrice(this.price, menuProduct);
            this.addMenuProduct(menuProduct);
        }
    }

    private void addMenuProduct(final MenuProduct menuProduct) {
        menuProduct.addedBy(this);
        menuProducts.add(menuProduct);
    }

    private void validateMenuPrice(Price menuPrice, MenuProduct menuProduct) {
        BigDecimal productPrice = menuProduct.getProduct().getPrice();
        Long productQuantity = menuProduct.getQuantity();

        if (menuPrice.isLarger(productPrice.multiply(BigDecimal.valueOf(productQuantity)))) {
            throw new IllegalArgumentException(EXCEPTION_MESSAGE_MENU_PRICE_INVALID);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.values();
    }
}
