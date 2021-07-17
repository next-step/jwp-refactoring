package kitchenpos.menu.handler;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.NotFoundProductException;
import kitchenpos.wrap.Price;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuMapper {

    public Menu mapFormToMenu(MenuRequest menuRequest, MenuGroup menuGroup) {
        MenuProducts menuProducts = mapFromToMenuProducts(menuRequest.getMenuProductRequests());
        Price menuPrice = new Price(menuRequest.getPrice());
        return new Menu(menuRequest.getName(), menuPrice, menuGroup, menuProducts);
    }

    private MenuProducts mapFromToMenuProducts(List<MenuProductRequest> menuProductRequests) {
        if(menuProductRequests.isEmpty()){
            throw new NotFoundProductException();
        }
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(menuProductRequest -> toMenuProduct(menuProductRequest))
                .collect(Collectors.toList());
        return new MenuProducts(menuProducts);
    }

    private MenuProduct toMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(menuProductRequest.getProductId(), menuProductRequest.getQuantity());
    }
}
