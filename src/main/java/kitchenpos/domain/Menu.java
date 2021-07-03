package kitchenpos.domain;

import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Menu {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false))
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    private Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
        menuProducts.toMenu(this);
    }

    public static Menu create(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return create(name, price, menuGroup, new MenuProducts(menuProducts));
    }

    public static Menu create(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        if (menuProducts.isMoreExpensiveThan(price)) {
            throw new IllegalArgumentException("메뉴의 가격이 메뉴와 연결된 상품의 수량 * 가격 보다 비쌀 수 없습니다.");
        }
        return new Menu(name, price, menuGroup, menuProducts);
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

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public Long getMenuGroupId() {
        return this.menuGroup.getId();
    }

    MenuGroup getMenu() {
        return this.menuGroup;
    }
}
