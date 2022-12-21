package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.common.constants.ErrorCodeType.MENU_PRICE_NOT_OVER_SUM_PRICE;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;

    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }


    public void validCheckMeuProductPrice() {
        BigDecimal sumPrice = this.menuProducts.getSumPrice();

        if (price.getPrice().compareTo(sumPrice) > 0) {
            throw new IllegalArgumentException(MENU_PRICE_NOT_OVER_SUM_PRICE.getMessage());
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


    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getList();
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menuGroupId=" + menuGroupId +
                ", menuProducts=" + menuProducts +
                '}';
    }
}
