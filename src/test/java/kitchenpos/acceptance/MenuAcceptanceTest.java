package kitchenpos.acceptance;

import static kitchenpos.acceptance.AcceptanceTestHelper.assertCreatedStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertInternalServerErrorStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertOkStatus;
import static kitchenpos.acceptance.MenuAcceptanceTestHelper.createMenu;
import static kitchenpos.acceptance.MenuAcceptanceTestHelper.getMenus;
import static kitchenpos.acceptance.MenuAcceptanceTestHelper.mapToMenuProduct;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestHelper.createMenuGroup;
import static kitchenpos.acceptance.ProductAcceptanceTestHelper.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroup menuGroup;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @Override
    @BeforeEach
    protected void setup() {
        super.setup();
        menuGroup = createMenuGroup("메뉴그룹").as(MenuGroup.class);
        menuProduct1 = mapToMenuProduct(createProduct("피자", BigDecimal.ONE).as(Product.class), 1);
        menuProduct2 = mapToMenuProduct(createProduct("파스타", BigDecimal.ONE).as(Product.class), 1);
    }

    @Test
    void 생성시_가격이_상품총금액미만이며_메뉴그룹과_메뉴상품이_모두등록된경우_생성성공() {
        assertCreatedStatus(createMenu( "메뉴", BigDecimal.ONE, menuGroup, Arrays.asList(menuProduct1, menuProduct2)));
    }

    @Test
    void 생성시_가격이_존재하지않는경우_생성실패() {
        final BigDecimal menuPrice = null;
        assertInternalServerErrorStatus(createMenu( "메뉴", menuPrice, menuGroup, Arrays.asList(menuProduct1, menuProduct2)));
    }

    @Test
    void 생성시_가격이_0원미만인경우_생성실패() {
        final BigDecimal menuPrice = BigDecimal.valueOf(-1);
        assertInternalServerErrorStatus(createMenu("메뉴", menuPrice, menuGroup, Arrays.asList(menuProduct1, menuProduct2)));
    }

    @Test
    void 생성시_가격이_상품총금액보다_높은경우_생성실패() {
        final BigDecimal menuPrice = BigDecimal.TEN;
        assertInternalServerErrorStatus(createMenu("메뉴", menuPrice, menuGroup, Arrays.asList(menuProduct1, menuProduct2)));
    }

    @Test
    void 생성시_메뉴그룹이_등록되지않은경우_생성실패() {
        menuGroup = new MenuGroup("미등록 메뉴그룹");
        assertInternalServerErrorStatus(createMenu("메뉴", BigDecimal.ONE, menuGroup, Arrays.asList(menuProduct1, menuProduct2)));
    }

    @Test
    void 생성시_등록되지않은_상품을_포함하는경우_생성실패() {
        menuProduct1 = new MenuProduct(new Product("미등록 메뉴", BigDecimal.ONE).getId(), 1);
        assertInternalServerErrorStatus(createMenu("메뉴", BigDecimal.ONE, menuGroup, Arrays.asList(menuProduct1, menuProduct2)));
    }

    @Test
    void 조회시_존재하는목록반환() {
        final Menu created = createMenu("메뉴", BigDecimal.ONE, menuGroup,
            Arrays.asList( menuProduct1, menuProduct2)).as(Menu.class);
        final ExtractableResponse<Response> response = getMenus();
        final List<Menu> menus = response.as(new TypeRef<List<Menu>>(){});
        assertAll(
            () -> assertOkStatus(response),
            () -> assertThat(mapToMenuIds(menus)).contains(created.getId())
        );
    }

    private List<Long> mapToMenuIds(List<Menu> menus) {
        return menus.stream()
            .map(Menu::getId)
            .collect(Collectors.toList());
    }
}
