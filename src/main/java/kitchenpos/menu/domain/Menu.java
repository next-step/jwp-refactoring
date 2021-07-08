package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;

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
import javax.persistence.OneToMany;

import kitchenpos.manugroup.domain.MenuGroup;
import kitchenpos.menu.exception.MenuException;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menuGroupId", foreignKey = @ForeignKey(name = "fk_menu_group_to_menu"), nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {

    }
    public Menu(String name, Price price, long menuGroupId, MenuProducts menuProducts) {

    }

    public Menu(long id, String name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(id, name, price, menuGroup);
        this.menuProducts = menuProducts;
    }

    private void validatePrice() {
        if(price.compareTo(this.menuProducts.getSumMenuProductPrice()) > 0) {
            throw new MenuException("메뉴의 가격이 메뉴 상품들의 총합보다 클 수 없습니다.");
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
