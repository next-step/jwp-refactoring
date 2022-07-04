package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class MenuProductRequest {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    private static Long setProductIdFromMenuProduct(MenuProduct menuProduct) {
        Long productId = null;
        if(menuProduct.getProduct()!= null){
            productId = menuProduct.getProduct().getId();
        }
        return productId;
    }

    private static Long setMenuIdFromMenuProduct(MenuProduct menuProduct) {
        Long menuId = null;
        if(menuProduct.getMenu()!= null){
            menuId = menuProduct.getMenu().getId();
        }
        return menuId;
    }

    public static MenuProductRequest of(MenuProduct menuProduct){
        Long productId = setProductIdFromMenuProduct(menuProduct);
        Long menuId = setMenuIdFromMenuProduct(menuProduct);

        return MenuProductRequest.builder().seq(menuProduct.getSeq())
                .productId(productId)
                .quantity(menuProduct.getQuantity())
                .menuId(menuId)
                .build();
    }
}
