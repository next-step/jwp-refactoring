package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_등록되어_있음;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_목록_조회_요청;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.acceptance.MenuGroupRestAssured;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.acceptance.ProductRestAssured;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 관련 인수 테스트")
class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 두마리메뉴;
    private ProductResponse 후라이드;
    private MenuProductRequest 후라이드치킨_상품;

    /**
     * Given 메뉴 그룹이 등록되어 있음
     * And 상품이 등록되어 있음
     */
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        두마리메뉴 = MenuGroupRestAssured.메뉴_그룹_등록되어_있음(MenuGroupRequest.from("두마리메뉴")).as(MenuGroupResponse.class);
        후라이드 = ProductRestAssured.상품_등록되어_있음(ProductRequest.of("후라이드", BigDecimal.valueOf(16_000))).as(ProductResponse.class);
        후라이드치킨_상품 = MenuProductRequest.of(후라이드.getId(), 2);
    }

    /**
     * When 메뉴 생성 요청
     * Then 메뉴 생성됨
     */
    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(후라이드치킨_상품);
        MenuRequest 후라이드치킨 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(16_000), 두마리메뉴.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(후라이드치킨);

        메뉴_생성됨(response);
    }

    /**
     * When 가격이 없는 메뉴 생성 요청
     * Then 메뉴 생성 실패함
     */
    @DisplayName("메뉴 가격이 없으면(null) 메뉴 생성이 실패한다.")
    @Test
    void createFail() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(후라이드치킨_상품);
        MenuRequest 가격없는_후라이드치킨 = MenuRequest.of("후라이드치킨", null, 두마리메뉴.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(가격없는_후라이드치킨);

        메뉴_생성_실패함(response);
    }

    /**
     * When 가격이 음수인 메뉴 생성 요청
     * Then 메뉴 생성 실패함
     */
    @DisplayName("메뉴 가격이 음수면 메뉴 생성이 실패한다.")
    @Test
    void createFail2() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(후라이드치킨_상품);
        MenuRequest 음수가격_후라이드치킨 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(-1), 두마리메뉴.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(음수가격_후라이드치킨);

        메뉴_생성_실패함(response);
    }

    /**
     * When 메뉴 그룹이 존재하지 않는 메뉴 생성 요청
     * Then 메뉴 생성 실패함
     */
    @DisplayName("메뉴의 메뉴그룹이 존재하지 않으면 메뉴 생성 실패한다.")
    @Test
    void createFail3() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(후라이드치킨_상품);
        MenuRequest 메뉴그룹_없는_후라이드치킨 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(16_000), 0L, 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(메뉴그룹_없는_후라이드치킨);

        메뉴_생성_실패함(response);
    }

    /**
     * When 상품에 등록되지 않은 메뉴 상품으로 메뉴 생성 요청
     * Then 메뉴 생성 실패함
     */
    @DisplayName("상품에 등록되지 않은 메뉴 상품으로 메뉴를 생성할 수 없다.")
    @Test
    void createFail4() {
        MenuProductRequest 상품_등록되어있지_않은_메뉴_상품 = MenuProductRequest.of(0L, 2);
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(상품_등록되어있지_않은_메뉴_상품);
        MenuRequest 후라이드치킨 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(16_000), 두마리메뉴.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(후라이드치킨);

        메뉴_생성_실패함(response);
    }

    /**
     * When 메뉴의 가격이 메뉴 상품들의 가격의 합보다 큰 메뉴 생성 요청
     * Then 메뉴 생성 실패함
     */
    @DisplayName("메뉴의 가격이 메뉴 상품들의 가격의 합보다 크면 메뉴를 생성할 수 없다.")
    @Test
    void createFail5() {
        MenuProductRequest 후라이드치킨_상품 = MenuProductRequest.of(후라이드.getId(), 2);
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(후라이드치킨_상품);
        MenuRequest 후라이드치킨 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(50_000), 두마리메뉴.getId(), 메뉴상품_목록);

        ExtractableResponse<Response> response = 메뉴_생성_요청(후라이드치킨);

        메뉴_생성_실패함(response);
    }

    /**
     * Given 메뉴 등록되어 있음
     * When 메뉴 목록 조회 요청
     * Then 메뉴 목록 조회됨
     */
    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        List<MenuProductRequest> 메뉴상품_목록 = Arrays.asList(후라이드치킨_상품);
        MenuRequest 후라이드치킨 = MenuRequest.of("후라이드치킨", BigDecimal.valueOf(16_000), 두마리메뉴.getId(), 메뉴상품_목록);
        메뉴_등록되어_있음(후라이드치킨);

        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        메뉴_목록_조회됨(response);
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_생성_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        List<MenuResponse> menus = response.jsonPath().getList(".", MenuResponse.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> Assertions.assertThat(menus).hasSize(1)
        );
    }
}
