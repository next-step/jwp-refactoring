package kitchenpos.dto.request;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.dto.dto.MenuProductDTO;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDTO> menuProducts = new LinkedList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProductDTO> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(List<MenuProductDTO> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public Map<Long, Long> getQuantityPerProduct() {
        return menuProducts.stream()
            .collect(Collectors.toMap(
                MenuProductDTO::getProductId,
                MenuProductDTO::getQuantity
            ));
    }
}
