package kitchenpos.application.creator;

import kitchenpos.domain.MenuGroup;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class MenuGroupHelper {

    public static MenuGroup create(String name) {
        MenuGroup group = new MenuGroup();
        group.setName(name);
        return group;
    }

}
