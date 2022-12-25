package kitchenpos.menu.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        validateName(name);
        validatePrice(price);
        validateMenuGroupId(menuGroupId);
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    private void validateName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("상품 이름은 필수값 입니다.");
        }
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuGroupId(Long menuGroupId) {
        if (Objects.isNull(menuGroupId) || menuGroupId == 0) {
            throw new IllegalArgumentException("메뉴 그룹이 비어있습니다.");
        }
    }

    public void addMenuProduct(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(this, menuProducts);
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.get();
    }
}
