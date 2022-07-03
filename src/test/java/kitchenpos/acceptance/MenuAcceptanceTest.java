package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.menus.menugroup.dto.MenuGroupResponse;
import kitchenpos.menus.menu.dto.MenuProductRequest;
import kitchenpos.menus.menu.dto.MenuRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 관련 기능 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    static private final String MENU_URL = "/api/menus";

    private ProductResponse 짬뽕;
    private ProductResponse 짜장;
    private MenuGroupResponse 중식;

    @TestFactory
    Stream<DynamicTest> menuTest() {
        return Stream.of(
                dynamicTest("메뉴를 생성 할 수 있다.", () -> {
                    //given
                    ExtractableResponse<Response> 짬뽕_생성_요청 = 상품_생성_요청("짬뽕", BigDecimal.valueOf(10000));
                    짬뽕 = 짬뽕_생성_요청.as(ProductResponse.class);
                    ExtractableResponse<Response> 짜장_생성_요청 = 상품_생성_요청("짜장", BigDecimal.valueOf(10000));
                    짜장 = 짜장_생성_요청.as(ProductResponse.class);
                    ExtractableResponse<Response> 중식_생성_요청_응답 = 메뉴그룹_생성_요청("중식");
                    중식 = 중식_생성_요청_응답.as(MenuGroupResponse.class);

                    //when & then
                    ExtractableResponse<Response> 메뉴_생성_요청_응답 = 메뉴_생성_요청("중식_메뉴", 5000L,
                            중식.getId(),
                            makeMenuProductRequests(짬뽕, 짜장));
                    메뉴_생성됨(메뉴_생성_요청_응답);

                }),
                dynamicTest("시스템에 없는 상품이 포함된 메뉴를 등록 한다.", () -> {
                    ProductResponse 없는_상품 = ProductResponse.of(1000L, "없는 상품", BigDecimal.valueOf(100L));
                    ExtractableResponse<Response> 잘못된_메뉴 = 메뉴_생성_요청("잘못된_메뉴", 5000L, 중식.getId(),
                            Arrays.asList(MenuProductRequest.of(없는_상품.getId(), 1L)));
                    메뉴_생성_실패됨(잘못된_메뉴);
                }),
                dynamicTest("상품의 총합계보다 비싼 메뉴 가격을 등록 할 수 없다.", () -> {
                    ExtractableResponse<Response> 잘못된_메뉴_생성_요청_응답 = 메뉴_생성_요청("잘못된_메뉴", 50000L,
                            중식.getId(),
                            makeMenuProductRequests(짬뽕, 짜장));
                    메뉴_생성_실패됨(잘못된_메뉴_생성_요청_응답);
                }),
                dynamicTest("생성 하려는 메뉴 가격은 0원 이상이어야 한다.", () -> {
                    ExtractableResponse<Response> 잘못된_메뉴_생성_요청_응답 = 메뉴_생성_요청("잘못된_메뉴", -1L,
                            중식.getId(),
                            makeMenuProductRequests(짬뽕, 짜장));
                    메뉴_생성_실패됨(잘못된_메뉴_생성_요청_응답);
                }),
                dynamicTest("메뉴의 목록을 조회 할 수 있다.", () -> {
                    ExtractableResponse<Response> 메뉴_목록_조회_요청 = 메뉴_목록_조회_요청();
                    메뉴_목록__조회됨(메뉴_목록_조회_요청);
                })
        );
    }

    private List<MenuProductRequest> makeMenuProductRequests(ProductResponse product1, ProductResponse product2) {
        return Arrays.asList(MenuProductRequest.of(product1.getId(), 1L),
                MenuProductRequest.of(product2.getId(), 1L));
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, Long price, Long menuGroupId,
                                                         List<MenuProductRequest> menuProducts) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(MenuRequest.of(name, price, menuGroupId, menuProducts))
                .when().post(MENU_URL)
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get(MENU_URL)
                .then().log().all()
                .extract();
    }

    public static void 메뉴_목록__조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


}
