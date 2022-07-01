package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_등록_요청;
import static kitchenpos.menu.acceptance.MenuRestAssured.메뉴_목록_조회_요청;
import static kitchenpos.menu.acceptance.MenuRestAssured.상품_등록_요청;
import static kitchenpos.menugroup.acceptance.MenuGroupRestAssured.메뉴그룹_등록_요청;
import static kitchenpos.utils.DomainFixtureFactory.createMenuGroupRequest;
import static kitchenpos.utils.DomainFixtureFactory.createMenuRequest;
import static kitchenpos.utils.DomainFixtureFactory.createProductRequest;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.utils.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends AcceptanceTest {
    private MenuRequest 양념치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();

        MenuGroupResponse 한마리메뉴 = 메뉴그룹_등록_요청(createMenuGroupRequest("한마리메뉴")).as(MenuGroupResponse.class);
        ProductResponse 양념 = 상품_등록_요청(createProductRequest( "양념", BigDecimal.valueOf(20000L))).as(ProductResponse.class);
        ProductResponse 후라이드 = 상품_등록_요청(createProductRequest( "후라이드", BigDecimal.valueOf(30000L))).as(ProductResponse.class);
        양념치킨 = createMenuRequest("양념치킨", BigDecimal.valueOf(40000L), 한마리메뉴.getId(), Lists.newArrayList(new MenuProductRequest(양념.getId(), 2L), new MenuProductRequest(후라이드.getId(), 3L)));
    }

    /**
     * When 메뉴를 등록 요청하면
     * Then 메뉴가 등록 됨
     */
    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청(양념치킨);

        // then
        메뉴_등록됨(response);
    }

    /**
     * Given 메뉴를 등록하고
     * When 메뉴 목록을 조회 하면
     * Then 메뉴 목록 조회 됨
     */
    @DisplayName("메뉴 목록을 조회 한다.")
    @Test
    void lists() {
        // given
        MenuResponse 등록한_양념치킨 = 메뉴_등록_요청(양념치킨).as(MenuResponse.class);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회됨(response, Lists.newArrayList(등록한_양념치킨));
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response, List<MenuResponse> expectedMenus) {
        List<MenuResponse> menus = response.jsonPath().getList(".", MenuResponse.class);
        Assertions.assertThat(menus).containsExactlyElementsOf(expectedMenus);
    }
}
