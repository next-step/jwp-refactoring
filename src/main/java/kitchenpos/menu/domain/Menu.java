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
    private static final int FREE = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer price;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(Long id, String name, Integer price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Menu(Long id, String name, Integer price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.addMenuProduct(menuProduct);
        menuProduct.setMenu(this);
    }

    public void checkAmount() {
        Amounts amounts = menuProducts.getAmounts();
        if (price > amounts.calculateTotalAmount()) {
            throw new IllegalArgumentException("[ERROR] 메뉴 가격은 총 금액을 넘을 수 없습니다.");
        }
    }

    private void validatePrice(Integer price) {
        if (price == null || price < FREE) {
            throw new IllegalArgumentException("[ERROR] 메뉴 가격은 0원 이상 이어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
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
