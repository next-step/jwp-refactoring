package kitchenpos.application.creator;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuGroupDto;
import kitchenpos.dto.MenuProductDto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class MenuHelper {

    public static MenuDto create(String name, int price, MenuGroupDto menuGroup, MenuProductDto...products) {
        MenuDto menu = new MenuDto();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroup.getId());
        menu.setMenuProducts(Arrays.asList(products));
        return menu;
    }
}
