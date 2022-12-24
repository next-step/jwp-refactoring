package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long menuId;
    private String name;
    @Embedded
    private MenuPrice price;

    protected OrderMenu() {
    }

    private OrderMenu(Long id, Long menuId, String name, MenuPrice price) {
        this.id = id;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public static OrderMenu generate(Long menuId, String name, BigDecimal price) {
        return new OrderMenu(null, menuId, name, MenuPrice.from(price));
    }

    public static OrderMenu of(Long id, Long menuId, String name, BigDecimal price) {
        return new OrderMenu(id, menuId, name, MenuPrice.from(price));
    }

    public boolean hasMenuId(Long menuId) {
        return Objects.equals(this.menuId, menuId);
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return price;
    }
}
