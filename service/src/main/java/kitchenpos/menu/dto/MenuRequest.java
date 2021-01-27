package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private String name;
    @NotNull
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    public MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public MenuRequest() {
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
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

    public List<MenuProduct> createMenuProducts(List<Product> products) {
        List<MenuProduct> createMenuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProducts) {
            Product product = getProduct(products, menuProductRequest.getProductId());
            createMenuProducts.add(new MenuProduct(product, menuProductRequest.getQuantity()));
        }
        return createMenuProducts;
    }

    public void validateSizeForProducts(List<Product> products) {
        if (CollectionUtils.isEmpty(products)) {
            throw new IllegalArgumentException("메뉴 등록에 요청된 상품이 존재하지 않습니다.");
        }
        if (products.size() != getSizeOfProducts()) {
            throw new IllegalArgumentException("메뉴 등록에 요청된 상품 중 등록 안된 상품이 존재합니다.");
        }
    }

    public int getSizeOfProducts() {
        if (menuProducts == null) {
            return 0;
        }
        return menuProducts.size();
    }

    private Product getProduct(List<Product> products, Long productId) {
        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("메뉴로 등록하려는 상품이 존재하지 않습니다."));
    }
}
