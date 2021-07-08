package kitchenpos.domain.menuproduct;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuCreate;
import kitchenpos.domain.menu.MenuCreateValidator;
import kitchenpos.domain.product.Products;
import kitchenpos.exception.MenuCheapException;
import org.springframework.stereotype.Component;

@Component
public class MenuAmountCreateValidator implements MenuCreateValidator {
    @Override
    public void validate(Menu menu, Products products, MenuCreate create) throws RuntimeException {
        MenuProducts menuProducts = MenuProducts.create(create.getMenuProducts(), menu, products);
        if (menu.getPrice().compareTo(menuProducts.sumAmount()) > 0) {
            throw new MenuCheapException();
        }
    }
}
