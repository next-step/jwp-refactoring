package kitchenpos.application.creator;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class MenuHelper {

    public static MenuCreateRequest createRequest(String name, Integer price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        BigDecimal bigDecimal = null;
        if (price != null) {
            bigDecimal = BigDecimal.valueOf(price);
        }
        return new MenuCreateRequest(name, bigDecimal, menuGroupId, menuProductRequests);
    }
}
