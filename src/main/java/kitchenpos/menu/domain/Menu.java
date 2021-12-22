package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.exception.PriceValueNotAcceptableException;

@Entity
public class Menu {

    private static final String ERROR_MESSAGE_MENU_PRICE_HIGH = "메뉴 가격은 상품 리스트의 가격 합보다 작거나 같아야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @JoinColumn(name = "menu_group_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;


    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST,
        CascadeType.MERGE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this(null, name, price, menuGroup, null);
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        validatePrice(price);

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        changeMenuProducts(menuProducts);
    }

    public void changeMenuProducts(List<MenuProduct> inputMenuProducts) {
        menuProducts.clear();
        if (isEmptyList(inputMenuProducts)) {
            validatePriceIsZero();
            return;
        }
        validatePriceIsLessThanMenuProductsSum(inputMenuProducts);

        for (MenuProduct menuProduct : inputMenuProducts) {
            menuProduct.assignMenu(this);
        }
        menuProducts.addAll(inputMenuProducts);
    }

    private boolean isEmptyList(List<MenuProduct> inputMenuProducts) {
        return Objects.isNull(inputMenuProducts) || inputMenuProducts.size() == 0;
    }

    private void validatePriceIsLessThanMenuProductsSum(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
            .map(MenuProduct::getMenuProductPrice)
            .reduce(BigDecimal.ZERO, (subSum, menuProductPrice) -> subSum.add(menuProductPrice));

        if (price.compareTo(sum) > 0) {
            throw new PriceValueNotAcceptableException(ERROR_MESSAGE_MENU_PRICE_HIGH);
        }
    }

    private void validatePriceIsZero() {
        if (price.compareTo(BigDecimal.ZERO) > 0) {
            throw new PriceValueNotAcceptableException("메뉴상품이 없는 경우 메뉴 가격은 0 이어야 합니다.");
        }
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

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new PriceValueNotAcceptableException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Menu)) {
            return false;
        }

        Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


}
