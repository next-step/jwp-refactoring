package kitchenpos.menuGroup.dto;

import javax.validation.constraints.NotNull;

public class MenuGroupRequest {
    @NotNull(message = "메뉴 그룹 이름은 필수값입니다.")
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
