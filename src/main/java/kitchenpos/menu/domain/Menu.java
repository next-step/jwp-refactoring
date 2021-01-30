package kitchenpos.menu.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY )
    private MenuGroup menuGroup;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public void validationCheck(boolean exitMenuGroupId) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("잘못된 가격을 입력하셨습니다.");
        }

        if (!exitMenuGroupId) {
            throw new IllegalArgumentException("메뉴그룹이 지정되지 않았습니다.");
        }
    }

    public void compare(BigDecimal sum) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격이 틀렸습니다.");
        }
    }

    public void changeMenuProducts(List<MenuProduct> savedMenuProducts) {
        menuProducts = savedMenuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
