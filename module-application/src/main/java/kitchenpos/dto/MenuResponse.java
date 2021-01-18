package kitchenpos.dto;



import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {
    }

    private MenuResponse(final Long id, final String name, final BigDecimal price,
                         final Long menuGroupId, final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(final Menu menu) {
        Long menuId = menu.getId();
        return new MenuResponse(menuId,
                menu.getName(),
                menu.getPrice()
                        .getAmount(),
                menu.getMenuGroup()
                        .getId(),
                MenuProductResponse.ofList(menu.getMenuProducts(), menuId)
        );
    }

    public static List<MenuResponse> ofList(final List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
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

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }

    public static class MenuProductResponse {

        private Long seq;
        private Long menuId;
        private Long productId;
        private long quantity;

        public MenuProductResponse() {
        }

        private MenuProductResponse(final Long seq, final Long menuId, final Long productId, final long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static MenuProductResponse of(final MenuProduct menuProduct, final Long menuId) {
            return new MenuProductResponse(menuProduct.getId(),
                    menuId,
                    menuProduct.getProduct()
                            .getId(),
                    menuProduct.getQuantity());
        }

        public static List<MenuProductResponse> ofList(final List<MenuProduct> menuProducts, final Long menuId) {
            return menuProducts.stream()
                    .map(menuProduct -> of(menuProduct, menuId))
                    .collect(Collectors.toList());
        }

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
    }
}
