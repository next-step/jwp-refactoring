package kitchenpos.menu.validator;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dao.ProductDao;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductDao productDao;

    public MenuValidator(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void validateCreate(Menu menu){

    }
}
