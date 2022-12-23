package kitchenpos.menu.fixture;

import kitchenpos.menugroup.fixture.MenuGroupTestFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import org.springframework.util.ReflectionUtils;
import kitchenpos.product.domain.Product;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.menu.fixture.MenuProductTestFixture.*;

public class MenuTestFixture {

    public static MenuRequest 메뉴세트요청(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return MenuRequest.of(name, price, menuGroupId, menuProducts);
    }

    public static MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청() {
        return 메뉴세트요청("짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), MenuGroupTestFixture.중국집1인메뉴세트그룹(MenuGroupTestFixture.중국집1인메뉴세트그룹요청()).getId(),
                Arrays.asList(짬뽕메뉴상품요청(), 탕수육메뉴상품요청(), 단무지메뉴상품요청()));
    }

    public static MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청() {
        return 메뉴세트요청("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), MenuGroupTestFixture.중국집1인메뉴세트그룹(MenuGroupTestFixture.중국집1인메뉴세트그룹요청()).getId(),
                Arrays.asList(짜장면메뉴상품요청(), 탕수육메뉴상품요청(), 단무지메뉴상품요청()));
    }

    public static Menu 메뉴세트(MenuRequest request, long id) {
        List<MenuProduct> menuProducts = 메뉴세트목록(request.getName(), request.getPrice(), request.getMenuProductsRequest());
        Menu menu = Menu.of(request.getName(), request.getPrice(), 1L, MenuProducts.from(menuProducts));
        setId(id, menu);
        return menu;
    }

    private static void setId(final long id, final Menu menu) {
        Field idField = Objects.requireNonNull(ReflectionUtils.findField(Menu.class, "id"));
        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(idField, menu, id);
    }

    private static List<MenuProduct> 메뉴세트목록(String name, BigDecimal price, final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(o -> o.toMenuProduct(Product.of(name, price)))
                .collect(Collectors.toList());
    }
}
