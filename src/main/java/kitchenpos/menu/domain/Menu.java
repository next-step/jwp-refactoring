package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.Price;
import kitchenpos.order.domain.OrderMenu;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price = new Price(BigDecimal.ZERO);
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(this, menuProducts);
        validateMenu();
    }

    public OrderMenu convertToOrderMenu() {
        return new OrderMenu(this.id, this.getName(), this.price);
    }

    private void validateMenu() {
        Price sumOfProductsPrice = menuProducts.sumOfProductsPrice();
        if (price.isGreaterThan(sumOfProductsPrice)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품들의 가격 합보다 크면 안된다");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.price();
    }

    public Long menuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> menuProducts() {
        return menuProducts.getMenuProducts();
    }
}
