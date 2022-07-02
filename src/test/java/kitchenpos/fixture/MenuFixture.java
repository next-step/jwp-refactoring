package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;

public class MenuFixture {

    public static Menu 메뉴_생성(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProductList) {
        return new Menu(name, price, menuGroup, menuProductList);
    }
    public static MenuRequest 메뉴_생성_요청(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProductList) {
        List<MenuProductRequest> menuProductRequestList = menuProductList.stream()
                .map(menuProduct -> new MenuProductRequest(menuProduct.getSeq(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
        return new MenuRequest(name, price, menuGroup.getId(), menuProductRequestList);
    }

    public static MenuProduct 메뉴상품_생성(Long seq, Product product, Long quantity) {
        return new MenuProduct(seq, product, quantity);
    }

    public static MenuGroup 메뉴그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroupRequest 메뉴그룹_생성_요청(String name) {
        return new MenuGroupRequest(name);
    }
}
