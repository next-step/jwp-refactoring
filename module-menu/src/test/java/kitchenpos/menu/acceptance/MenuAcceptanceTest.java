package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Menu;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 인수테스트 기능")
class MenuAcceptanceTest extends AcceptanceTest {
    private static final String MENU_URI = "/api/menus";
    private static final String PRODUCT_URI = "/api/products";

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
        final MenuGroup 예상된_메뉴그룹= new MenuGroup(1L, "후라이드세트");
        final List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct.Builder(null)
                .setSeq(1L)
                .setProductId(1L)
                .setQuantity(Quantity.of(2L))
                .build());
        final Menu 예상된_메뉴 = new Menu.Builder("후라이드")
                .setPrice(Price.of(16_000L))
                .setMenuGroup(예상된_메뉴그룹)
                .setMenuProducts(new MenuProducts(menuProducts))
                .build();
        final MenuProduct 예상된_메뉴_제품들 = new MenuProduct.Builder(예상된_메뉴)
                .setSeq(1L)
                .setProductId(1L)
                .setQuantity(Quantity.of(2L))
                .build();
        final MenuResponse 예상된_메뉴_결과 = new MenuResponse(1L, "반반후라이드", Price.of(16_000L), 예상된_메뉴그룹.toMenuGroupResponse(),
                Arrays.asList(예상된_메뉴_제품들));
        메뉴_그룹_생성_요청("후라이드세트");
        제품_생성_요청("후라이드", 16_000L);
        final List<MenuProductRequest> 메뉴_제품들 = Arrays.asList(new MenuProductRequest(1L, 2));

        // when
        final ExtractableResponse<Response> 메뉴_생성_요청_결과 = 메뉴_생성_요청("반반후라이드", 16_000L, 1L, 메뉴_제품들);
        메뉴_생성_요청_확인(메뉴_생성_요청_결과);

        // then
        final ExtractableResponse<Response> 메뉴_조회_결과 = 메뉴_조회();
        메뉴_조회_확인(메뉴_조회_결과, Arrays.asList(예상된_메뉴_결과));
    }

    public static ExtractableResponse<Response> 제품_생성_요청(String 제품명, Long 금액) {
        final ProductRequest 생성할_제품 = new ProductRequest(제품명, 금액);
        return RestAssuredHelper.post(PRODUCT_URI, 생성할_제품);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String 메뉴명, Long 메뉴_금액, Long 메뉴_그룹_아이디,
                                                         List<MenuProductRequest> 메뉴_제품들) {
        final MenuRequest menuRequest = new MenuRequest(메뉴명, 메뉴_금액, 메뉴_그룹_아이디, 메뉴_제품들);
        return RestAssuredHelper.post(MENU_URI, menuRequest);
    }

    public static void 메뉴_생성_요청_확인(ExtractableResponse<Response> 메뉴_생성_요청_결과) {
        assertThat(메뉴_생성_요청_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_조회() {
        return RestAssuredHelper.get(MENU_URI);
    }

    private void 메뉴_조회_확인(ExtractableResponse<Response> 메뉴_조회_결과, List<MenuResponse> 예상된_메뉴_조회_결과) {
        final List<MenuResponse> actual = 메뉴_조회_결과.body().jsonPath().getList(".", MenuResponse.class);

        assertAll(
                () -> assertThat(메뉴_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual).hasSize(예상된_메뉴_조회_결과.size()),
                () -> 메뉴_내용_확인(actual, 예상된_메뉴_조회_결과)
        );
    }

    private void 메뉴_내용_확인(List<MenuResponse> 메뉴_리스트1, List<MenuResponse> 메뉴_리스트2) {
        for (int idx = 0; idx < 메뉴_리스트1.size(); idx++) {
            int innerIdx = idx;
            assertAll(
                    () -> assertThat(메뉴_리스트1.get(innerIdx).getName()).isEqualTo(메뉴_리스트2.get(innerIdx).getName()),
                    () -> assertThat(메뉴_리스트1.get(innerIdx).getPrice()).isEqualTo(메뉴_리스트2.get(innerIdx).getPrice()),
                    () -> assertThat(메뉴_리스트1.get(innerIdx).getMenuGroup().getId())
                            .isEqualTo(메뉴_리스트2.get(innerIdx).getMenuGroup().getId()),
                    () -> assertThat(메뉴_리스트1.get(innerIdx).getMenuProducts())
                            .hasSize(메뉴_리스트2.get(innerIdx).getMenuProducts().size())
            );
        }
    }
}
