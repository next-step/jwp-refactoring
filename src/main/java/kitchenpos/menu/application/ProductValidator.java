package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.common.Price;
import kitchenpos.menu.dto.MenuProductRequest;

public interface ProductValidator {
    void checkOverPrice(Price price, List<MenuProductRequest> menuProductRequests);

}
