package kitchenpos.domain;

import kitchenpos.common.ErrorCode;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {}

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        validation(name, menuGroup);

        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.addMenuProduct(this, menuProduct);
    }

    private void validation(String name, MenuGroup menuGroup) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_MENU_NAME.getErrorMessage());
        }
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_MENU_GROUP.getErrorMessage());
        }
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
        return Objects.equals(name, menu.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public Long getId() {
        return getId();
    }

    public String getName() {
        return name;
    }

    public void setMenuProducts(List<MenuProduct> savedMenuProducts) {
    }

    public void setId(long id) {
    }

    public void setName(String name) {
    }

    public void setPrice(BigDecimal price) {
    }

    public void setMenuGroupId(long menu_group_id) {
    }
}
