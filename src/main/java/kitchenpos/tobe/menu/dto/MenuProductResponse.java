package kitchenpos.tobe.menu.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tobe.menu.domain.MenuProduct;

public class MenuProductResponse {

    private final Long seq;

    private final Long productId;

    private final BigDecimal price;

    private final long quantity;

    public MenuProductResponse(
        final Long seq,
        final Long productId,
        final BigDecimal price,
        final long quantity
    ) {
        this.seq = seq;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public static List<MenuProductResponse> ofList(final List<MenuProduct> menuProduct) {
        return menuProduct.stream()
            .map(MenuProductResponse::of)
            .collect(Collectors.toList());
    }

    public static MenuProductResponse of(final MenuProduct menuProduct) {
        return new MenuProductResponse(
            menuProduct.getSeq(),
            menuProduct.getProductId(),
            menuProduct.getPrice(),
            menuProduct.getQuantity()
        );
    }
}
