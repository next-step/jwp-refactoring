package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MenuProductResponse {
    private Long seq;
    private ProductResponse product;
    private long quantity;

    public MenuProductResponse(Long seq, ProductResponse product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), new ProductResponse(menuProduct.getProduct().getId()), menuProduct.getQuantity());
    }

    public static List<MenuProductResponse> ofList(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductResponse::of)
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductResponse that = (MenuProductResponse) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, product, quantity);
    }
}
