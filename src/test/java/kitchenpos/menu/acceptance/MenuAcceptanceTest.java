package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuRequest menuRequest;
    private List<MenuProductRequest> menuProductRequests = new ArrayList<>();
    private ProductRequest 강정치킨 = new ProductRequest("강정치킨상품", new BigDecimal(8000));
    private ProductRequest 양념치킨 = new ProductRequest("양념치킨상품", new BigDecimal(7000));
    private MenuGroup menuGroup = new MenuGroup("치킨");

    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> createResponse = 메뉴그룹_등록되어_있음(menuGroup);
        menuProductRequests.add(new MenuProductRequest(상품_등록되어_있음(강정치킨).as(Product.class).getId(),1L));
        menuProductRequests.add(new MenuProductRequest(상품_등록되어_있음(양념치킨).as(Product.class).getId(),1L));
        menuRequest = new MenuRequest("강정치킨", new BigDecimal(15000), createResponse.as(MenuGroup.class).getId(), menuProductRequests);
    }

    @DisplayName("메뉴를 관리한다.")
    @Test
    void manageMenu() {
        //menu create
        ExtractableResponse<Response> createResponse = 메뉴_등록_요청(menuRequest);
        메뉴_등록됨(createResponse);

        //menu find
        ExtractableResponse<Response> findResponse = 메뉴목록_조회_요청();
        메뉴목록_조회됨(findResponse);
    }

    private ExtractableResponse<Response> 상품_등록되어_있음(ProductRequest product) {
        return RestAssured.given().log().all().
                body(product).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/products").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 메뉴그룹_등록되어_있음(MenuGroup menuGroup) {
        return RestAssured.given().log().all().
                body(menuGroup).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/menu-groups").
                then().log().all().
                extract();
    }


    private ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest request) {
        return RestAssured.given().log().all().
                body(request).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/menus").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 메뉴목록_조회_요청() {
        return RestAssured.given().log().all().
                when().get("/api/menus").
                then().log().all().
                extract();
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(MenuResponse.class).getName()).isEqualTo("강정치킨");
        assertThat(response.as(MenuResponse.class).getPrice().compareTo(new BigDecimal(15000))).isEqualTo(0);
    }

    private void 메뉴목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<MenuResponse> menus = response.jsonPath().getList(".", MenuResponse.class);
        List<String> menuNames = menus.stream().map(menu -> menu.getName()).collect(Collectors.toList());

        assertThat(menuNames).contains("강정치킨");
    }

}
