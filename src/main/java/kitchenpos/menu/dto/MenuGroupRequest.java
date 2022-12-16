package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupRequest {

    private String name;

    private MenuGroupRequest() {}

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup.Builder()
                .name(name)
                .build();
    }

    public String getName() {
        return name;
    }

    public static class Builder {

        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroupRequest build() {
            return new MenuGroupRequest(name);
        }
    }
}
