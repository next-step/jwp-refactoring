package kitchenpos.menu.dto;

import kitchenpos.menu.domain.Menu;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * packageName : kitchenpos.dto
 * fileName : MenuResponse
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class MenuResponse {
    private Long id;
    private String name;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    private MenuResponse() {
    }

    private MenuResponse(Long id, String name, Long menuGroupId, List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuResponse of(Menu menu) {
        final List<MenuProductResponse> menuProductResponses = MenuProductResponse.ofList(menu.getMenuProducts());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getMenuGroupId(), menuProductResponses);
    }

    public static List<MenuResponse> ofList(List<Menu> menus) {
        return menus.stream()
                .map(MenuResponse::of)
                .collect(toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
