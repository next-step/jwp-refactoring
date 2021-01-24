package kitchenpos.menu.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.product.dto.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private final long id;
    private final String productName;
    private final long quantity;

    public MenuProductResponse(long id, String productName, long quantity) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> ofProducts(List<MenuProduct> saveAll) {
        return saveAll.stream()
                .map(MenuProductResponse::ofProduct)
                .collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public long getQuantity() {
        return quantity;
    }

    public static MenuProductResponse ofProduct(MenuProduct product) {
        return new MenuProductResponse(product.getId(), ProductResponse.of(product.getProduct()).getName(), product.getQuantity());
    }
}
