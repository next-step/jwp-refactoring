package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kitchenpos.fixture.MenuGroupTestFixture.중국집_1인_메뉴_세트;
import static kitchenpos.fixture.MenuGroupTestFixture.중국집_1인_메뉴_세트_요청;
import static kitchenpos.fixture.MenuProductTestFixture.*;

public class MenuTestFixture {

    public static MenuRequest createMenu(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return MenuRequest.of(name, price, menuGroupId, menuProducts);
    }

    public static MenuRequest 짬뽕_탕수육_1인_메뉴_세트_요청() {
        return createMenu("짬뽕_탕수육_1인_메뉴_세트", BigDecimal.valueOf(21000L), 중국집_1인_메뉴_세트(중국집_1인_메뉴_세트_요청()).getId(),
                Arrays.asList(짬뽕메뉴상품(), 탕수육메뉴상품(), 단무지메뉴상품()));
    }

    public static MenuRequest 짜장면_탕수육_1인_메뉴_세트_요청() {
        return createMenu("짜장면_탕수육_1인_메뉴_세트", BigDecimal.valueOf(20000L), 중국집_1인_메뉴_세트(중국집_1인_메뉴_세트_요청()).getId(),
                Arrays.asList(짜장면메뉴상품(), 탕수육메뉴상품(), 단무지메뉴상품()));
    }

    public static Menu 메뉴_세트_생성(MenuRequest request, long id) {
        List<MenuProduct> menuProducts = getMenuProducts(request.getName(), request.getPrice(), request.getMenuProducts());
        Menu menu = Menu.of(request.getName(), request.getPrice(), 중국집_1인_메뉴_세트(중국집_1인_메뉴_세트_요청()), MenuProducts.from(menuProducts));
        Field idField = Objects.requireNonNull(ReflectionUtils.findField(Menu.class, "id"));
        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(idField, menu, id);
        return menu;
    }

    private static List<MenuProduct> getMenuProducts(String name, BigDecimal price, final List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(o -> o.toMenuProduct(Product.of(name, price)))
                .collect(Collectors.toList());
    }
}
