package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validEmpty(name, menuGroup);
        validPrice(price, menuProducts.getSumPrice());
        this.name = name;
        this.menuGroup = menuGroup;
        this.price = price;
        this.menuProducts = menuProducts.mapMenu(this);
    }

    public static Menu of(String name, int price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        return new Menu(name, Price.of(BigDecimal.valueOf(price)), menuGroup,
            MenuProducts.of(menuProducts));
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.add(menuProducts);
        price.validPriceGreaterThanMin(this.menuProducts.getSumPrice());
    }

    private void validEmpty(String name, MenuGroup menuGroup) {
        if (Objects.isNull(name)) {
            throw new InvalidParameterException("메뉴이름이 비어 있을 수 없습니다.");
        }

        if (Objects.isNull(menuGroup)) {
            throw new InvalidParameterException("메뉴그룹이 비어 있을 수 없습니다.");
        }
    }

    public boolean isSame(Long menuId) {
        return this.id.equals(menuId);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    private void validPrice(Price price, BigDecimal sumPrice) {
        price.validPriceGreaterThanMin(sumPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (Objects.isNull(id)) {
            return false;
        }

        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
