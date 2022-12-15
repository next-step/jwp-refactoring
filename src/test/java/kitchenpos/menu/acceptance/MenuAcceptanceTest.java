package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.*;
import kitchenpos.product.acceptance.ProductAcceptance;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성을_요청;
import static kitchenpos.product.acceptance.ProductAcceptance.상품_생성을_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 치킨;
    private ProductResponse 닭다리;
    private ProductResponse 소스;
    private List<MenuProductRequest> 메뉴상품;

    @BeforeEach
    public void setUp() {
        super.setUp();
        닭다리 = 상품_생성을_요청("닭다리", new BigDecimal(25000)).as(ProductResponse.class);
        소스 = 상품_생성을_요청("소스", new BigDecimal(18000)).as(ProductResponse.class);
        메뉴상품 = Arrays.asList(new MenuProductRequest(닭다리.getId(), 1L), new MenuProductRequest(소스.getId(), 2L));
        치킨 = 메뉴그룹_생성을_요청("치킨").as(MenuGroupResponse.class);
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void createMenu() {
        ExtractableResponse<Response> response = 메뉴_생성을_요청("양념치킨", new BigDecimal(50000), 치킨.getId(), 메뉴상품);

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    @DisplayName("메뉴 그룹이 없는 메뉴를 생성하면 실패")
    @Test
    void createMenuWithNullMenuGroup() {
        ExtractableResponse<Response> response = 메뉴_생성을_요청("후라이드치킨", new BigDecimal(50000), null, 메뉴상품);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("존재하지 않는 상품을 포함하는 메뉴를 생성한다")
    @Test
    void createMenuWithNullProduct() {
        메뉴상품 = new ArrayList<>(메뉴상품);
        메뉴상품.add(new MenuProductRequest(-1L, 1L));

        ExtractableResponse<Response> response = 메뉴_생성을_요청("양념치킨", new BigDecimal(50000), 치킨.getId(), 메뉴상품);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("메뉴의 가격을 음수 값으로 하여 메뉴를 생성한다")
    @Test
    void createMenuWithNegativePrice() {
        ExtractableResponse<Response> response = 메뉴_생성을_요청("간장치킨", new BigDecimal(-50000), 치킨.getId(), 메뉴상품);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("메뉴 상품의 총 금액의 합보다 큰 값을 메뉴의 가격으로 하여 메뉴를 생성할경우 실패")
    @Test
    void createMenuWithInvalidPrice() {
        ExtractableResponse<Response> response = 메뉴_생성을_요청("먹물치킨", new BigDecimal(90000), 치킨.getId(), 메뉴상품);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("메뉴 목록을 조회한다")
    @Test
    void getMenuList() {
        메뉴_생성을_요청("현미치킨", new BigDecimal(50000), 치킨.getId(), 메뉴상품);
        메뉴_생성을_요청("먹물치킨", new BigDecimal(50000), 치킨.getId(), 메뉴상품);

        ExtractableResponse<Response> response = 메뉴_목록을_요청();

        assertThat(response.jsonPath().getList(".", MenuResponse.class)).hasSize(2);
    }

    public static ExtractableResponse<Response> 메뉴_생성을_요청(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        MenuRequest request = new MenuRequest(name, price, menuGroupId, menuProductRequests);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록을_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }
}
