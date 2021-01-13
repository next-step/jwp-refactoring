package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse(final Long id, final String name, final BigDecimal price,
                         final Long menuGroupId, final List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(final Menu menu) {
        return new MenuResponse(menu.getId(),
                menu.getName(),
                menu.getPrice()
                        .getAmount(),
                menu.getMenuGroup()
                        .getId(),
                MenuProductResponse.ofList(menu.getMenuProducts())
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

        private MenuProductResponse(final Long seq, final Long menuId, final Long productId, final long quantity) {
            this.seq = seq;
            this.menuId = menuId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public static MenuProductResponse of(final MenuProduct menuProduct) {
            return new MenuProductResponse(menuProduct.getId(),
                    menuProduct.getMenu()
                            .getId(),
                    menuProduct.getProduct()
                            .getId(),
                    menuProduct.getQuantity());
        }

        public static List<MenuProductResponse> ofList(final List<MenuProduct> menuProducts) {
            return menuProducts.stream()
                    .map(MenuProductResponse::of)
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
