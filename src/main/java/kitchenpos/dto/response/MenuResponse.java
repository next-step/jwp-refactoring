package kitchenpos.dto.response;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.dto.dto.MenuProductDTO;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductDTO> menuProducts = new LinkedList<>();

    protected MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProductDTO> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        List<MenuProductDTO> menuProductDTOS = menu.getMenuProducts()
            .stream()
            .map(MenuProductDTO::of)
            .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
            menu.getMenuGroupId(), menuProductDTOS);
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

    public List<MenuProductDTO> getMenuProducts() {
        return menuProducts;
    }
}
