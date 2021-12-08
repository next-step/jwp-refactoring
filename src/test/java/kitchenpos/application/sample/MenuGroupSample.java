package kitchenpos.application.sample;

import kitchenpos.domain.MenuGroup;

public class MenuGroupSample {

    public static MenuGroup 면류() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("면류");
        return menuGroup;
    }

}
