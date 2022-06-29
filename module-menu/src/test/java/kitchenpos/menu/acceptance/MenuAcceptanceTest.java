package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.메뉴_등록_요청;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.메뉴_등록되어_있음;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.메뉴_등록됨;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.메뉴_목록_응답됨;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.메뉴_목록_조회_요청;
import static kitchenpos.menu.acceptance.MenuAcceptanceTestMethod.메뉴_목록_포함됨;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTestMethod.메뉴_그룹_등록되어_있음;
import static kitchenpos.menu.acceptance.ProductAcceptanceTestMethod.상품_등록되어_있음;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.menu.acceptance.code.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 관련 기능 인수테스트")
class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 초밥_메뉴그룹;
    private ProductResponse 우아한_초밥_A;
    private ProductResponse 우아한_초밥_B;

    private MenuRequest 우아한_초밥_메뉴_A_request;
    private MenuRequest 우아한_초밥_메뉴_B_request;

    @BeforeEach
    public void setUp() {
        super.setUp();
        초밥_메뉴그룹 = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("초밥_메뉴그룹")).as(MenuGroupResponse.class);
        우아한_초밥_A = 상품_등록되어_있음(ProductRequest.of("우아한_초밥_A", BigDecimal.valueOf(10_000))).as(ProductResponse.class);
        우아한_초밥_B = 상품_등록되어_있음(ProductRequest.of("우아한_초밥_B", BigDecimal.valueOf(20_000))).as(ProductResponse.class);

        MenuProductRequest 우아한_초밥_A_메뉴상품_request = MenuProductRequest.of(우아한_초밥_A.getId(), 2L);
        MenuProductRequest 우아한_초밥_B_메뉴상품_request = MenuProductRequest.of(우아한_초밥_B.getId(), 2L);

        우아한_초밥_메뉴_A_request = MenuRequest.of("우아한_초밥_메뉴_A", BigDecimal.valueOf(10_000), 초밥_메뉴그룹.getId(), Lists.newArrayList(우아한_초밥_A_메뉴상품_request));
        우아한_초밥_메뉴_B_request = MenuRequest.of("우아한_초밥_메뉴_B", BigDecimal.valueOf(10_000), 초밥_메뉴그룹.getId(), Lists.newArrayList(우아한_초밥_B_메뉴상품_request));
    }

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create01() {
        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청(우아한_초밥_메뉴_A_request);

        // then
        메뉴_등록됨(response);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        ExtractableResponse<Response> createdResponse1 = 메뉴_등록되어_있음(우아한_초밥_메뉴_A_request);
        ExtractableResponse<Response> createdResponse2 = 메뉴_등록되어_있음(우아한_초밥_메뉴_B_request);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response);
        메뉴_목록_포함됨(response, Lists.newArrayList(createdResponse1, createdResponse2));
    }
}