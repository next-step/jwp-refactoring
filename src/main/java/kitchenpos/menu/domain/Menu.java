package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20)")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validateMenuPrice(menuProducts, price);
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        setMenuProducts(menuProducts);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(final Price price) {
        this.price = price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    private void setMenuProducts(final MenuProducts menuProducts) {
        menuProducts.groupToMenu(this);
        this.menuProducts = menuProducts;
    }

    public void validateMenuPrice(MenuProducts menuProducts, Price menuPrice) {
        if (Objects.isNull(menuPrice) || menuProducts.priceSumIsLessThan(menuPrice)) {
            throw new IllegalArgumentException("메뉴 가격이 유효하지 않습니다.");
        }
    }

    public void changeInfo(String name, Price price) {
        this.name = name;
        this.price = price;
    }
}
