package kitchenpos.domain.menu;

import static kitchenpos.domain.validator.PriceValidator.validatePrice;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Menu {

    public static final String MENU_PRICE_IS_OVER_THAN_TOTAL_SUM_OF_MENU_PRODUCT_PRICE_ERROR_MESSAGE = "메뉴에 구성된 상품 가격의 총합 보다 메뉴 가격이 클 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "menu_group_id",
        foreignKey = @ForeignKey(name = "FK_MENU_TO_MENU_GROUP"),
        nullable = false
    )
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {

    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validatePrice(price);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.of(this, menuProducts);
        validateMenuPrice();
    }

    public void validateMenuPrice() {
        if (price.compareTo(menuProducts.totalMenuProductPrice()) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_IS_OVER_THAN_TOTAL_SUM_OF_MENU_PRODUCT_PRICE_ERROR_MESSAGE);
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
        return menuProducts.getMenuProducts();
    }
}
