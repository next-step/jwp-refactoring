package kitchenpos.dto.menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class MenuResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    public MenuResponse() {

    }

    private MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse from(Menu savedMenu) {
        // TODO MenuProducts 일급 컬렉션을 이용한 MenuProductResponse 생성 및 반환 처리
        List<MenuProductResponse> menuProductResponses = savedMenu.getMenuProducts().stream()
            .map(MenuProductResponse::from)
            .collect(Collectors.toList());
        return new MenuResponse(
            savedMenu.getId(),
            savedMenu.getName(),
            savedMenu.getPrice(),
            savedMenu.getMenuGroup().getId(),
            menuProductResponses
        );
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
}
