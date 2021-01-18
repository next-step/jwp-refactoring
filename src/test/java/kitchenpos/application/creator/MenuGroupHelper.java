package kitchenpos.application.creator;

import kitchenpos.domain.model.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class MenuGroupHelper {

    public static MenuGroup create(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupCreateRequest createRequest(String name) {
        return new MenuGroupCreateRequest(name);
    }

}
