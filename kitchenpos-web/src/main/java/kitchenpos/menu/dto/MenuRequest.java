package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuProductDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    private Long id;
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

    public MenuRequest(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductDto> getMenuProducts() {
        return menuProducts.stream()
                .map(menuProductRequest -> new MenuProductDto(menuProductRequest.getProductId(), menuProductRequest.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(menuProductRequest -> menuProductRequest.getProductId())
                .collect(Collectors.toList());
    }
}
