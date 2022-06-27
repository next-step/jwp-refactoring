package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;

import javax.persistence.*;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private long price;
    @Column(nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu(String name, long price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;

        for (final MenuProductRequest menuProductRequest : menuProducts) {
            this.menuProducts.add(new MenuProduct(this, menuProductRequest.getProductId(), menuProductRequest.getQuantity()));
        }
    }

    protected Menu() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts.getAll();
    }
}
