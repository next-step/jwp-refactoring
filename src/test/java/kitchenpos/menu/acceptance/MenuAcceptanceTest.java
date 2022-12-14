package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.menu.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 관련 기능 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 양식;
    private ProductResponse 스테이크;
    private ProductResponse 스파게티;
    private ProductResponse 에이드;
    private List<MenuProductRequest> menuProducts;

    @BeforeEach
    public void setUp() {
        super.setUp();

        양식 = 메뉴_그룹_생성_요청("양식").as(MenuGroupResponse.class);
        스테이크 = 상품_생성_요청("스테이크", new BigDecimal(25000)).as(ProductResponse.class);
        스파게티 = 상품_생성_요청("스파게티", new BigDecimal(18000)).as(ProductResponse.class);
        에이드 = 상품_생성_요청("에이드", new BigDecimal(3500)).as(ProductResponse.class);

        menuProducts = Arrays.asList(new MenuProductRequest(스테이크.getId(), 1L),
                new MenuProductRequest(스파게티.getId(), 1L),
                new MenuProductRequest(에이드.getId(), 2L));
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴 생성을 요청하면
     * Then 메뉴가 생성된다.
     */
    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response =
                메뉴_생성_요청("양식 세트", new BigDecimal(50000), 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    /**
     * Given 상품을 생성하고
     * When 메뉴 그룹이 존재하지 않는 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴 그룹이 존재하지 않는 메뉴를 생성한다.")
    @Test
    void createMenuWithNullMenuGroup() {
        // when
        ExtractableResponse<Response> response =
                메뉴_생성_요청("양식 세트", new BigDecimal(50000), null, menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * When 존재하지 않는 상품을 포함하는 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("존재하지 않는 상품을 포함하는 메뉴를 생성한다.")
    @Test
    void createMenuWithNullProduct() {
        // given
        menuProducts = new ArrayList<>(menuProducts);
        menuProducts.add(new MenuProductRequest(-1L, 1L));

        // when
        ExtractableResponse<Response> response =
                메뉴_생성_요청("양식 세트", new BigDecimal(50000), 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴의 이름을 빈 값으로 하여 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴의 이름을 빈 값으로 하여 메뉴를 생성한다.")
    @Test
    void createMenuWithNullName() {
        // when
        ExtractableResponse<Response> response =
                메뉴_생성_요청(null, new BigDecimal(50000), 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴의 가격을 빈 값으로 하여 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴의 가격을 빈 값으로 하여 메뉴를 생성한다.")
    @Test
    void createMenuWithNullPrice() {
        // when
        ExtractableResponse<Response> response =
                메뉴_생성_요청("양식 세트", null, 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴의 가격을 음수 값을 하여 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴의 가격을 음수 값으로 하여 메뉴를 생성한다.")
    @Test
    void createMenuWithNegativePrice() {
        // when
        ExtractableResponse<Response> response =
                메뉴_생성_요청("양식 세트", new BigDecimal(-50000), 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴 상품의 총 금액의 합보다 큰 값을 메뉴의 가격으로 하여 메뉴 생성을 요청하면
     * Then 메뉴를 생성할 수 없다.
     */
    @DisplayName("메뉴 상품의 총 금액의 합보다 큰 값을 메뉴의 가격으로 하여 메뉴를 생성한다.")
    @Test
    void createMenuWithInvalidPrice() {
        // when
        ExtractableResponse<Response> response =
                메뉴_생성_요청("양식 세트", new BigDecimal(60000), 양식.getId(), menuProducts);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    /**
     * Given 메뉴 그룹을 생성하고
     * And 상품을 생성하고
     * When 메뉴 목록을 조회하면
     * Then 메뉴 목록이 조회된다.
     */
    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        메뉴_생성_요청("양식 세트", new BigDecimal(50000), 양식.getId(), menuProducts);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        assertThat(response.jsonPath().getList(".", MenuResponse.class)).hasSize(1);
    }
    
    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId,
                                                         List<MenuProductRequest> menuProductRequests) {
        MenuRequest request = MenuRequest.of(name, price, menuGroupId, menuProductRequests);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }
}
