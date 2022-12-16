package kitchenpos.acceptance;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.rest.MenuGroupRestAssured;
import kitchenpos.rest.MenuRestAssured;
import kitchenpos.rest.ProductRestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuAcceptanceTest extends BaseAcceptanceTest {

    private Product 후라이드;
    private MenuGroup 한마리메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        this.후라이드 = ProductRestAssured.상품_등록됨(ProductFixture.후라이드).as(Product.class);
        this.한마리메뉴 = MenuGroupRestAssured.메뉴_그룹_등록됨(MenuGroupFixture.한마리메뉴).as(MenuGroup.class);
    }

    @Test
    void 신규_메뉴_정보가_주어진_경우_메뉴_등록_요청시_요청에_성공한다() {
        // given
        String 메뉴명 = "후라이드치킨";
        BigDecimal 메뉴가격 = 후라이드.getPrice();
        Long 메뉴_아이디 = 한마리메뉴.getId();
        List<MenuProduct> 메뉴_상품목록 = Arrays.asList(new MenuProduct(null, 후라이드.getId(), 2L));

        // when
        ExtractableResponse<Response> response = MenuRestAssured.메뉴_등록_요청(
                메뉴명,
                메뉴가격,
                메뉴_아이디,
                메뉴_상품목록
        );

        // then
        메뉴_등록됨(response, 메뉴명, 메뉴가격, 메뉴_아이디, 메뉴_상품목록);
    }

    @Test
    void 메뉴_목록_조회_요청시_요청에_성공한다() {
        // given
        Menu 한마리메뉴 = MenuRestAssured.메뉴_등록됨(this.후라이드, this.한마리메뉴, MenuFixture.후라이드치킨, 2L).as(Menu.class);

        // when
        ExtractableResponse<Response> response = MenuRestAssured.메뉴_목록_조회();

        // then
        메뉴_목록_조회됨(response, 한마리메뉴.getName());
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response, String menuName, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = response.as(Menu.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(menu.getName()).isEqualTo(menuName),
                () -> assertThat(menu.getPrice().floatValue()).isEqualTo(price.floatValue()),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroupId),
                () -> assertThat(menu.getMenuProducts()).hasSize(menuProducts.size())
        );
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response, String... expectedMenuNames) {
        List<Menu> menus = response.as(new TypeRef<List<Menu>>() {});
        List<String> menuNames = menus.stream()
                .map(Menu::getName)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(menus).hasSize(1),
                () -> assertThat(menuNames).containsExactly(expectedMenuNames)
        );
    }
}
