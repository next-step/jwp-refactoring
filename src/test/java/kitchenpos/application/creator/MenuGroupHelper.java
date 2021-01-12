package kitchenpos.application.creator;

import kitchenpos.dto.MenuGroupDto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class MenuGroupHelper {

    public static MenuGroupDto create(String name) {
        MenuGroupDto group = new MenuGroupDto();
        group.setName(name);
        return group;
    }

}
