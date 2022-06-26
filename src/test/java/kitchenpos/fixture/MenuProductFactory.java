package kitchenpos.fixture;

import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;

public class MenuProductFactory {
    public static MenuProductRequest createMenuProductRequest(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
    
}
