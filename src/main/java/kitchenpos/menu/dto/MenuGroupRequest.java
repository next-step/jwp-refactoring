package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

/**
 * packageName : kitchenpos.dto
 * fileName : MenuGroupRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class MenuGroupRequest {
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public static MenuGroupRequest of(String name) {
        return new MenuGroupRequest(name);
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }

}
