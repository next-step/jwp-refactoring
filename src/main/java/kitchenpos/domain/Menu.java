package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @Column(name = "menu_group_id")
    private Long menuGroupId;
    @Embedded
    private MenuProductBag menuProducts;

    private Menu(Name name, Price price, Long menuGroupId, MenuProductBag menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(Name name, Price price, Long menuGroupId, MenuProductBag menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public Menu() {
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
        return menuGroupId;
    }

    public MenuProductBag getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(name, menu.name) && Objects.equals(price.intValue(), menu.price.intValue())
                && Objects.equals(menuGroupId, menu.menuGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, menuGroupId, menuProducts);
    }

    public List<Product> productList() {
        return this.menuProducts.productList();
    }

    public void checkValidPrice() {
        Price sum = totalPrice();
        if (this.price.moreThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 가격의 총 합과 같거나 작아야합니다");
        }
    }

    private Price totalPrice() {
        return this.menuProducts.totalPrice();

    }

    public void setMenuToMenuProducts() {
        this.menuProducts.setMenuToMenuProducts(this);
    }
}
