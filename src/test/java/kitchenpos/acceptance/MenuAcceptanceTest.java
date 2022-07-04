package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.fixture.MenuFixtureFactory;
import kitchenpos.menu.fixture.MenuProductFixtureFactory;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.product.domain.Product;
import kitchenpos.acceptance.util.KitchenPosBehaviors;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class MenuAcceptanceTest extends AcceptanceTest {
    /**
     * Given 메뉴그룹 및 상품이 생성되어 있다. When 메뉴를 생성한다. Then 메뉴가 생성된다. When 메뉴목록을 조회한다. Then 메뉴목록이 조회된다.
     */
    @Test
    @DisplayName("메뉴생성 및 조회 기능 인수테스트")
    void menuAcceptanceTest() {
        MenuGroup menuGroup = KitchenPosBehaviors.메뉴그룹_생성됨("치킨");
        Product product = KitchenPosBehaviors.상품_생성됨("강정치킨", 10000);
        MenuProduct menuProduct = MenuProductFixtureFactory.createMenuProduct(product.getId(), 1);
        Menu menu = MenuFixtureFactory.createMenu(menuGroup, "강정치킨 한마리", 10000, Lists.newArrayList(menuProduct));

        ExtractableResponse<Response> createResponse = KitchenPosBehaviors.메뉴_생성_요청(menu);
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        List<MenuDto> menus = KitchenPosBehaviors.메뉴_목록조회();
        assertThat(menus).hasSize(1);
    }
}
