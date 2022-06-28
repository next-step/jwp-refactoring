package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    public Menu toMenu() {
        Menu menu = new Menu(name, price, menuGroupId);
        menu.addMenuProducts(menuProducts);
        return menu;
    }

    public void validate(List<Product> products) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        validateSum(products);
    }

    private void validateSum(List<Product> products) {
        BigDecimal sum = sumProductsPrice(products);
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal sumProductsPrice(List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final Product product : products) {
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(getMenuProductQuantity(product.getId()))));
        }
        return sum;
    }

    private long getMenuProductQuantity(Long productId) {
        return menuProducts.stream()
                .filter(m -> Objects.equals(m.getProductId(), productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuRequest that = (MenuRequest) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getPrice(), that.getPrice()) && Objects.equals(getMenuGroupId(), that.getMenuGroupId()) && Objects.equals(getMenuProducts(), that.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice(), getMenuGroupId(), getMenuProducts());
    }
}
