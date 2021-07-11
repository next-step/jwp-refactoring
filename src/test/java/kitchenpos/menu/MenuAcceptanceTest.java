package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.Product.ProductAccteptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.MenuGroupAcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuRequest menuRequest;
    private MenuGroupResponse givenMenuGroup;
    private ProductResponse givenProductOne;
    private ProductResponse givenProductTwo;


    @BeforeEach
    public void setUp() {
        super.setUp();
        givenMenuGroup = MenuGroupAcceptanceTest.메뉴그룹_등록되어_있음(new MenuGroupRequest("테스트메뉴그룹"));
        List<MenuProductRequest> menuProductRequests = generateMenuRequests();
        menuRequest = new MenuRequest("일번메뉴", BigDecimal.valueOf(1000), givenMenuGroup.getId(), menuProductRequests);
    }

    private List<MenuProductRequest> generateMenuRequests() {
        givenProductOne = ProductAccteptanceTest.상품_등록되어_있음(new ProductRequest("커피",BigDecimal.valueOf(3000)));
        givenProductTwo = ProductAccteptanceTest.상품_등록되어_있음(new ProductRequest("라면",BigDecimal.valueOf(5000)));
        MenuProductRequest firstMenuProduct = new MenuProductRequest(givenProductOne.getId(), 1);
        MenuProductRequest secondMenuProduct = new MenuProductRequest(givenProductOne.getId(), 2);
        return Arrays.asList(firstMenuProduct, secondMenuProduct);
    }

    @DisplayName("Dto와 JPA를 사용하여 메뉴를 등록할 수 있다")
    @Test
    void createTest() {
        //when
        ExtractableResponse<Response> response = 메뉴_등록_요청(menuRequest);

        //then
        정상_등록(response);
        MenuResponse menuResponse = response.as(MenuResponse.class);
        assertThat(menuRequest.getName()).isEqualTo(menuResponse.getName());
    }

    @DisplayName("Dto와 JPA를 사용하여 메뉴를 조회할 수 있다")
    @Test
    void listTest() {
        //given
        메뉴_등록_요청(menuRequest);

        //when
        ExtractableResponse<Response> response = 메뉴_조회_요청(menuRequest);

        //then
        정상_처리(response);
        List<String> menuNames = response.jsonPath().getList(".", MenuResponse.class).stream()
                .map(MenuResponse::getName)
                .collect(Collectors.toList());
        assertThat(menuNames).contains(menuRequest.getName());
    }

    private ExtractableResponse<Response> 메뉴_조회_요청(MenuRequest menuRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus/temp")
                .then().log().all()
                .extract();
    }

    public static MenuResponse 메뉴_등록되어_있음(MenuRequest menuRequest) {
        ExtractableResponse<Response> response = 메뉴_등록_요청(menuRequest);
        정상_등록(response);
        return response.as(MenuResponse.class);

    }


    private static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menuRequest) {
        return RestAssured
                .given().log().all()
                .body(menuRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus/temp")
                .then().log().all()
                .extract();

    }

}
