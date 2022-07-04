package kitchenpos.menu.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

public class MenuFixture {

    public static Menu 메뉴_생성(String name, Price price, Long menuGroupId, List<MenuProduct> menuProductList) {
        return new Menu(name, price, menuGroupId, menuProductList);
    }

    public static MenuRequest 메뉴요청_생성(String name, BigDecimal price, Long menuGroupId,
                                      List<MenuProduct> menuProductList) {
        List<MenuProductRequest> menuProductRequestList = menuProductList.stream()
                .map(menuProduct -> new MenuProductRequest(menuProduct.getSeq(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
        return new MenuRequest(name, price, menuGroupId, menuProductRequestList);
    }

    public static MenuProduct 메뉴상품_생성(Long seq, Long productId, Long quantity) {
        return new MenuProduct(seq, productId, quantity);
    }

    public static MenuGroup 메뉴그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroupRequest 메뉴그룹_생성_요청(String name) {
        return new MenuGroupRequest(name);
    }

    public static List<MenuProduct> 파스타메뉴_상품_생성() {
        return Arrays.asList(
                메뉴상품_생성(1L, 1L, 1L),
                메뉴상품_생성(2L, 2L, 1L)
        );
    }
}
