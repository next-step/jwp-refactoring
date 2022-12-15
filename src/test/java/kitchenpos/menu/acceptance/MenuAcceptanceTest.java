package kitchenpos.menu.acceptance;

import static kitchenpos.menu.dto.MenuProductRequestTest.메뉴상품_요청_객체_생성;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private ProductResponse 소머리국밥;
    private ProductResponse 순대국밥;
    private MenuGroupResponse 식사_메뉴그룹;

    @BeforeEach
    public void setUp() {
        super.setUp();

        소머리국밥 = 상품_등록되어_있음("소머리국밥", BigDecimal.valueOf(8000)).as(ProductResponse.class);
        순대국밥 = 상품_등록되어_있음("순대국밥", BigDecimal.valueOf(7000)).as(ProductResponse.class);
        식사_메뉴그룹 = 메뉴그룹_등록되어_있음("식사").as(MenuGroupResponse.class);

        MenuProductRequest 순대국밥_메뉴상품 = 메뉴상품_요청_객체_생성(순대국밥.getId(), 1L);
        메뉴_등록되어_있음("순대국밥", BigDecimal.valueOf(7000), 식사_메뉴그룹.getId(), Arrays.asList(순대국밥_메뉴상품));
    }


    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void create() {
        // given
        String 메뉴명 = "소머리국밥";
        BigDecimal 가격 = BigDecimal.valueOf(8000);
        Long 메뉴그룹_아이디 = 식사_메뉴그룹.getId();
        MenuProductRequest 소머리국밥_메뉴상품 = 메뉴상품_요청_객체_생성(소머리국밥.getId(), 1L);
        List<MenuProductRequest> 메뉴상품들 = Arrays.asList(소머리국밥_메뉴상품);

        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청(메뉴명, 가격, 메뉴그룹_아이디, 메뉴상품들);

        // then
        메뉴_등록됨(response);
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_등록되어_있음(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return 메뉴_등록_요청(name, price, menuGroupId, menuProductRequests);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);
        params.put("menuGroupId", menuGroupId);
        params.put("menuProducts", menuProductRequests);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
