package kitchenpos.menugroup.dto;

import kitchenpos.menugroup.domain.MenuGroup;

/**
 * packageName : kitchenpos.dto
 * fileName : MenuGroupRequest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
public class MenuGroupRequest {
    private String name;

    private MenuGroupRequest() {
    }

    private MenuGroupRequest(String name) {
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
