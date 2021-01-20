package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.acceptance.ProductAcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroup 추천메뉴;
    private ProductResponse 양념치킨;
    private ProductResponse 후라이드치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();
        MenuGroupRequest 추천메뉴 = new MenuGroupRequest("추천메뉴");
        ProductRequest product_양념치킨 = new ProductRequest("양념치킨", BigDecimal.valueOf(16000));
        ProductRequest product_후라이드치킨 = new ProductRequest("후라이드치킨", BigDecimal.valueOf(15000));

        this.추천메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음(추천메뉴).as(MenuGroup.class);
        this.양념치킨 = ProductAcceptanceTest.상품_등록되어_있음(product_양념치킨).as(ProductResponse.class);
        this.후라이드치킨 = ProductAcceptanceTest.상품_등록되어_있음(product_후라이드치킨).as(ProductResponse.class);
    }

    @DisplayName("메뉴를 관리한다.")
    @Test
    void manageMenu() {
        // given
        MenuProductRequest menuProductRequest_양념 = new MenuProductRequest(양념치킨.getId(), 1);
        MenuProductRequest menuProductRequest_후라이드 = new MenuProductRequest(후라이드치킨.getId(), 1);

        MenuRequest 잘못된_가격 = new MenuRequest("후라이드+양념", BigDecimal.valueOf(32000), 추천메뉴.getId(),
                Arrays.asList(menuProductRequest_양념, menuProductRequest_후라이드));
        MenuRequest 올바른_가격 = new MenuRequest("후라이드+양념", BigDecimal.valueOf(31000), 추천메뉴.getId(),
                Arrays.asList(menuProductRequest_양념, menuProductRequest_후라이드));

        // when
        ExtractableResponse<Response> wrongResponse = 메뉴_등록_요청(잘못된_가격);

        // then
        메뉴_생성_실패됨(wrongResponse);

        // when
        ExtractableResponse<Response> createResponse = 메뉴_등록_요청(올바른_가격);

        // then
        메뉴_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(findResponse);
        메뉴_목록_포함됨(findResponse, Arrays.asList(createResponse));
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menuRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_등록_되어있음(String menuName, BigDecimal menuPrice, MenuGroup menuGroup, List<MenuProductRequest> menuProductRequests) {
        return 메뉴_등록_요청(new MenuRequest("양념+후라이드", menuPrice, menuGroup.getId(), menuProductRequests));
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_생성_실패됨(ExtractableResponse<Response> wrongResponse) {
        assertThat(wrongResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private void 메뉴_목록_응답됨(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 메뉴_목록_포함됨(ExtractableResponse<Response> findResponse, List<ExtractableResponse<Response>> createResponse) {
        List<Long> createMenuIds = createResponse.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> findMenuIds = findResponse.jsonPath().getList("id", Long.class);
        assertThat(findMenuIds).containsAll(createMenuIds);
    }
}
