package kitchenpos.ui;

import static kitchenpos.ui.MenuGroupAcceptanceTestFixture.메뉴_그룹_생성되어있음;
import static kitchenpos.ui.ProductAcceptanceTestFixture.상품_생성_되어있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTestFixture extends AcceptanceTest {

    public ProductResponse 떡볶이;
    public ProductResponse 튀김;
    public ProductResponse 순대;

    public MenuProductRequest 떡튀순_상품_떡볶이;
    public MenuProductRequest 떡튀순_상품_튀김;
    public MenuProductRequest 떡튀순_상품_순대;
    public MenuProductRequest 떡튀순_곱배기_상품_떡볶이;

    public List<MenuProductRequest> 떡튀순_상품_목록;
    public List<MenuProductRequest> 떡튀순_곱배기_상품_목록;
    public MenuGroupResponse 세트;
    public MenuRequest 떡튀순;
    public MenuRequest 떡튀순_곱배기;

    @BeforeEach
    public void setUp() {
        super.setUp();

        세트 = 메뉴_그룹_생성되어있음(new MenuGroupRequest("세트"));

        떡볶이 = 상품_생성_되어있음(new ProductRequest("떡볶이", BigDecimal.valueOf(4500)));
        튀김 = 상품_생성_되어있음(new ProductRequest("튀김", BigDecimal.valueOf(2500)));
        순대 = 상품_생성_되어있음(new ProductRequest("순대", BigDecimal.valueOf(4000)));

        떡튀순_상품_떡볶이 = new MenuProductRequest(떡볶이.getId(), 1);
        떡튀순_상품_튀김 = new MenuProductRequest(튀김.getId(), 1);
        떡튀순_상품_순대 = new MenuProductRequest(순대.getId(), 1);
        떡튀순_곱배기_상품_떡볶이 = new MenuProductRequest(떡볶이.getId(), 2);

        떡튀순_상품_목록 = Arrays.asList(떡튀순_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);
        떡튀순_곱배기_상품_목록 = Arrays.asList(떡튀순_곱배기_상품_떡볶이, 떡튀순_상품_튀김, 떡튀순_상품_순대);

        떡튀순 = new MenuRequest("떡튀순", BigDecimal.valueOf(10000), 세트.getId(), 떡튀순_상품_목록);
        떡튀순_곱배기 = new MenuRequest("떡튀순_곱배기", BigDecimal.valueOf(10000), 세트.getId(), 떡튀순_곱배기_상품_목록);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static MenuResponse 메뉴_생성_되어있음(MenuRequest menuRequest) {
        return 메뉴(메뉴_생성_요청(menuRequest));
    }

    public static MenuResponse 메뉴(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", MenuResponse.class);
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static List<MenuResponse> 메뉴_목록(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("", MenuResponse.class);
    }

    public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
