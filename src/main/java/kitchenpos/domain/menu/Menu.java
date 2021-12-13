package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import kitchenpos.domain.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.utils.StreamUtils;

@Entity
@Table(name = "menu")
public class Menu {
    private static final String INVALID_MENU_PRICE = "Menu Price 는 상품 가격 총합보다 작아야합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {}

    public Menu(Long id) {
        this.id = id;
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu from(long id) {
        return new Menu(id);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup, new ArrayList<>());
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        validateMenuPrice(menuProducts);

        this.menuProducts.addAll(menuProducts);
        menuProducts.forEach(menuProduct -> menuProduct.assignMenu(this));
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    private void validateMenuPrice(List<MenuProduct> menuProducts) {
        BigDecimal sum = StreamUtils.sumToBigDecimal(menuProducts, MenuProduct::calculateTotalPrice);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException(INVALID_MENU_PRICE);
        }
    }
}
