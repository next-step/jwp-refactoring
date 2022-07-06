package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.MenuProductAcceptanceTest;
import kitchenpos.fixture.MenuFactory;
import kitchenpos.fixture.MenuProductFactory;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends MenuProductAcceptanceTest {
    private MenuGroupResponse 빅맥세트;
    private ProductResponse 토마토;
    private ProductResponse 양상추;
    private Menu 빅맥버거;

    /**
     * When 메뉴 그룹이 있다.
     * <p>
     * And 메뉴에 들어갈 상품이 있다.
     * <p>
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        빅맥세트 = 메뉴그룹_등록_요청("빅맥세트").as(MenuGroupResponse.class);
        토마토 = 상품_등록_요청("토마토", 1000).as(ProductResponse.class);
        양상추 = 상품_등록_요청("양상추", 500).as(ProductResponse.class);
    }

    /**
     * Feature: 메뉴를 관리한다.
     * <p>
     * Scenario: 메뉴 관리
     * <p>
     * When 메뉴 등록 요청
     * <p>
     * Then 메뉴 등록됨
     * <p>
     * When 메뉴 목록 조회 요청
     * <p>
     * Then 메뉴 목록이 조회됨
     */
    @Test
    void 메뉴관리() {

        ExtractableResponse<Response> response;
        // when 메뉴그룹 등록 요청
        response = 메뉴_등록_요청("빅맥버거", BigDecimal.valueOf(3000), 빅맥세트.getId(),
                Arrays.asList(
                        MenuProductFactory.createMenuProductRequest(토마토.getId(), 1),
                        MenuProductFactory.createMenuProductRequest(양상추.getId(), 4)
                )
        );

        // then 메뉴그룹 등록됨
        메뉴_등록됨(response);
        MenuResponse 빅맥버거 = response.as(MenuResponse.class);

        // when 메뉴그룹 목록 조회 요청
        response = 메뉴_목록_조회();
        // then 메뉴그룹 목록이 조회됨
        메뉴_목록_조회됨(response);
        // then 메뉴그룹 목록이 조회됨
        메뉴_목록_포함됨(response, Arrays.asList(빅맥버거));
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, BigDecimal price, Long menuGroupId,
                                                         List<MenuProductRequest> menuProductRequests) {

        MenuRequest request = MenuFactory.createMenuRequest(name, price, menuGroupId, menuProductRequests);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(Menu menu) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }


    public static void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<MenuResponse> expectedMenus) {
        List<Long> resultMenuIds = response.jsonPath().getList(".", MenuResponse.class).stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedMenuIds = expectedMenus.stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultMenuIds).containsAll(expectedMenuIds);
    }
}
