package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct2> menuProducts = new ArrayList<>();

    protected Menu2() {
    }

    public Menu2(String name, Money price, MenuGroup menuGroup, List<MenuProduct2> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        addMenuProducts(menuProducts);
    }

    private void addMenuProducts(List<MenuProduct2> menuProducts) {
        menuProducts.forEach(this::addMenuProduct);
    }

    private void addMenuProduct(MenuProduct2 addMenuProduct) {
        this.menuProducts.add(addMenuProduct);
        addMenuProduct.setMenu(this);
    }

    public List<MenuProduct2> getMenuProducts() {
        return menuProducts;
    }
}
