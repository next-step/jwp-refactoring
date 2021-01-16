package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.common.Price;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.MenuGroupAcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.ProductAcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 기능 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    public static MenuGroupResponse 오늘의메뉴;
    public static ProductResponse 후라이드;
    public static ProductResponse 양념치킨;
    public static MenuProductRequest 메뉴상품1;
    public static MenuProductRequest 메뉴상품2;


    @BeforeEach
    public void setUp() {
        super.setUp();
        오늘의메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록_요청("오늘의 메뉴").as(MenuGroupResponse.class);
        후라이드 = ProductAcceptanceTest.상품_등록_요청("후라이드", 15000).as(ProductResponse.class);
        양념치킨 = ProductAcceptanceTest.상품_등록_요청("양념치킨", 15000).as(ProductResponse.class);
        메뉴상품1 = new MenuProductRequest(오늘의메뉴.getId(), 후라이드.getId(), 1);
        메뉴상품2 = new MenuProductRequest(오늘의메뉴.getId(), 양념치킨.getId(), 1);
    }

    @Test
    @DisplayName("시나리오1: 메뉴를 등록하고 목록을 조회할 수 있다.")
    public void scenarioTest() throws Exception {
        // when 메뉴_등록_요청
        ExtractableResponse<Response> 메뉴_등록 = 메뉴_등록_요청("후양 두마리 세트", 28000, 오늘의메뉴, Arrays.asList(메뉴상품1, 메뉴상품2));
        // then 메뉴_등록됨
        메뉴_등록됨(메뉴_등록);

        // when 메뉴_목록_조회_요청
        ExtractableResponse<Response> 메뉴_목록 = 메뉴_목록_조회_요청();
        // then 메뉴_목록_조회됨
        메뉴_목록_조회됨(메뉴_목록);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, int price, MenuGroupResponse menuGroup, List<MenuProductRequest> menuProducts) {
        MenuRequest menuRequest = new MenuRequest(name, Price.of(price).get(), menuGroup.getId(), menuProducts);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
