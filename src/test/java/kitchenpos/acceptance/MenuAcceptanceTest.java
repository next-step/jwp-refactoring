package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.제품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.utils.RestAssuredHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 인수테스트 기능")
class MenuAcceptanceTest extends AcceptanceTest {
    private static final String MENU_URI = "/api/menus";

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    /**
     *  Given 메뉴 그룹이 생성되어 있고
     *    And 제품(상품)이 생성되어 있고
     *  When 메뉴를 생성하면
     *  Then 메뉴를 조회할 수 있다.
     */
    @Test
    @DisplayName("메뉴를 생성하면 메뉴를 조회 할 수 있다.")
    void searchMenu() {
        // given
        메뉴_그룹_생성_요청("후라이드세트");
        제품_생성_요청("후라이드", 16_000);
        final List<MenuProduct> 메뉴_제품들 = Arrays.asList(new MenuProduct(1L, 1L, 1L, 2));

        // when
        final ExtractableResponse<Response> 메뉴_생성_요청_결과 = 메뉴_생성_요청("반반후라이드", 16_000, 1L, 메뉴_제품들);
        메뉴_생성_요청_확인(메뉴_생성_요청_결과);

        // then
        final ExtractableResponse<Response> 메뉴_조회_결과 = 메뉴_조회();
        메뉴_조회_확인(메뉴_조회_결과, Arrays.asList(new Menu("반반후라이드", BigDecimal.valueOf(16_000.0), 1L, 메뉴_제품들)));
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String 메뉴명, Integer 메뉴_금액, Long 메뉴_그룹_아이디,
                                                         List<MenuProduct> 메뉴_제품들) {
        final Menu menu = new Menu(메뉴명, BigDecimal.valueOf(메뉴_금액), 메뉴_그룹_아이디, 메뉴_제품들);
        return RestAssuredHelper.post(MENU_URI, menu);
    }

    public static void 메뉴_생성_요청_확인(ExtractableResponse<Response> 메뉴_생성_요청_결과) {
        assertThat(메뉴_생성_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_조회() {
        return RestAssuredHelper.get(MENU_URI);
    }

    private void 메뉴_조회_확인(ExtractableResponse<Response> 메뉴_조회_결과, List<Menu> 예상된_메뉴_조회_결과) {
        final List<Menu> actual = 메뉴_조회_결과.body().jsonPath().getList(".", Menu.class);

        assertAll(
                () -> assertThat(메뉴_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual).hasSize(예상된_메뉴_조회_결과.size()),
                () -> 메뉴_내용_확인(actual, 예상된_메뉴_조회_결과)
        );
    }

    private void 메뉴_내용_확인(List<Menu> 메뉴_리스트1, List<Menu> 메뉴_리스트2) {
        for (int idx = 0; idx < 메뉴_리스트1.size(); idx++) {
            int innerIdx = idx;
            assertAll(
                    () -> assertThat(메뉴_리스트1.get(innerIdx).getName()).isEqualTo(메뉴_리스트2.get(innerIdx).getName()),
                    () -> assertThat(메뉴_리스트1.get(innerIdx).getPrice()).isEqualTo(메뉴_리스트2.get(innerIdx).getPrice()),
                    () -> assertThat(메뉴_리스트1.get(innerIdx).getMenuGroupId())
                            .isEqualTo(메뉴_리스트2.get(innerIdx).getMenuGroupId()),
                    () -> assertThat(메뉴_리스트1.get(innerIdx).getMenuProducts())
                            .hasSize(메뉴_리스트2.get(innerIdx).getMenuProducts().size())
            );
        }
    }
}
