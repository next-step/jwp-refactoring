package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private static final String 메뉴_그룹_이름_국밥 = "국밥";
    private static final String 상품_이름_순대 = "순대";
    private static final int 상품_가격 = 8000;
    public static final String 메뉴_이름_순대국 = "순대국";
    public static final int 메뉴_가격 = 8000;
    public static final Long 상품_수량 = 1L;

    private Long 메뉴_그룹_번호;
    private Long 상품_번호;

    @BeforeEach
    void setup() {
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_그룹_생성_요청(메뉴_그룹_이름_국밥);
        ExtractableResponse<Response> 상품_생성_요청_응답 = 상품_생성_요청(상품_이름_순대, 상품_가격);

        메뉴_그룹_번호 = 메뉴_그룹_번호_추출(메뉴_그룹_생성_요청_응답);
        상품_번호 = 상품_번호_추출(상품_생성_요청_응답);
    }

    @DisplayName("사용자는 메뉴를 생성 할 수 있다.")
    @Test
    void create() {
        // given
        // when
        ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답 = 메뉴_생성_요청(메뉴_이름_순대국, 메뉴_가격, 메뉴_그룹_번호, 상품_번호, 상품_수량);

        // then
        메뉴_그룹_생성_요청_응답_확인(메뉴_그룹_생성_요청_응답);
    }

    @DisplayName("사용자는 메뉴 전체를 조회 할 수 있다.")
    @Test
    void list() {
        // given
        메뉴_생성_요청(메뉴_이름_순대국, 메뉴_가격, 메뉴_그룹_번호, 상품_번호, 상품_수량);
        // when
        ExtractableResponse<Response> 메뉴_조회_요청_응답 = 메뉴_그룹_조회_요청();
        // then
        메뉴_조회_요청_응답_확인(메뉴_조회_요청_응답);
    }

    private ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/api/menus/")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_생성_요청(String 메뉴_이름, int 가격, Long 메뉴_그룹_번호, Long 상품_번호, Long 상품_수량) {
        MenuProductRequest 메뉴_상품_요청 = new MenuProductRequest(상품_번호, 상품_수량);
        MenuRequest menuRequest = new MenuRequest(메뉴_이름, 가격, 메뉴_그룹_번호, Arrays.asList(메뉴_상품_요청));
        return RestAssured.given().log().all()
                .body(menuRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/menus/")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_그룹_생성_요청(String 요청_메뉴_그룹) {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(요청_메뉴_그룹);
        return RestAssured.given().log().all()
                .body(menuGroupRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/menu-groups/")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 상품_생성_요청(String 상품_이름, int 상품_가격) {
        ProductRequest productRequest = new ProductRequest(상품_이름, 상품_가격);
        return RestAssured.given().log().all()
                .body(productRequest)
                .contentType(ContentType.JSON)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    private void 메뉴_그룹_생성_요청_응답_확인(ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답) {
        assertThat(메뉴_그룹_생성_요청_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private Long 상품_번호_추출(ExtractableResponse<Response> 상품_생성_요청_응답) {
        String[] locationInfo = 상품_생성_요청_응답.header("Location").split("/");
        return Long.parseLong(locationInfo[3]);
    }

    private Long 메뉴_그룹_번호_추출(ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답) {
        MenuGroupResponse 응답 = 메뉴_그룹_생성_요청_응답.as(MenuGroupResponse.class);
        return 응답.getId();
    }

    private void 메뉴_조회_요청_응답_확인(ExtractableResponse<Response> 메뉴_조회_요청_응답) {
        List<MenuResponse> menuResponses = 메뉴_조회_요청_응답.jsonPath().getList("", MenuResponse.class);
        MenuResponse menuResponse = menuResponses.get(0);
        assertThat(menuResponse.getName()).isEqualTo(메뉴_이름_순대국);
        assertThat(menuResponse.getMenuProductResponses().get(0).getQuantity()).isEqualTo(상품_수량);
        assertThat(menuResponse.getMenuProductResponses().get(0).getProductResponse().getPrice()).isEqualTo(상품_가격);
        assertThat(menuResponse.getMenuProductResponses().get(0).getProductResponse().getName()).isEqualTo(상품_이름_순대);
        assertThat(menuResponse.getMenuGroupResponse().getName()).isEqualTo(메뉴_그룹_이름_국밥);
    }
}
