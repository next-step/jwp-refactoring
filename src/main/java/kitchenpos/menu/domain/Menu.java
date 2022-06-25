package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
    @Column(nullable = false)
    private String name;
    @Embedded
    private MenuPrice menuPrice;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(Long id, String name, MenuPrice menuPrice) {
        this.id = id;
        this.name = name;
        this.menuPrice = menuPrice;
    }

    public Menu(Long id, String name, MenuPrice price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.menuPrice = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.addMenuProduct(menuProduct);
        menuProduct.setMenu(this);
    }

    public void checkAmount() {
        Amounts amounts = menuProducts.getAmounts();
        validateAmount(amounts);
    }

    private void validateAmount(Amounts amounts) {
        if (menuPrice.overTo(amounts)){
            throw new IllegalArgumentException("[ERROR] 메뉴 가격은 총 금액을 넘을 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return menuPrice;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void setMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

}
