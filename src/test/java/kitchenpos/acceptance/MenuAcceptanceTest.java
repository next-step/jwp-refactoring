package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴")
public class MenuAcceptanceTest extends AcceptanceTest {

    private ProductResponse 생맥주;
    private ProductResponse 닭강정;
    private MenuGroup 일식;

    @BeforeEach
    public void setUp() {
        super.setUp();
        생맥주 = 상품이_등록되어_있음(new ProductRequest("생맥주", BigDecimal.valueOf(2000)));
        닭강정 = 상품이_등록되어_있음(new ProductRequest("닭강정", BigDecimal.valueOf(3000)));
        일식 = 메뉴그룹이_등록되어있음(new MenuGroupRequest("일식"));
    }

/*  -- 메뉴 등록 관리
    given 상품이 2개가 등록 되어 있음
    given 메뉴를 구성할 상품과 수량을 결정
    given 등록할 메뉴 생성
    when 메뉴 등록 요청
    then 메뉴등록이 됨
    when 등록한_메뉴들을_조회
    then 등록한 메뉴가 조회됨*/
    @Test
    @DisplayName("메뉴를 등록 관리")
    void menuCrateManage() {
        //given
        MenuProduct 닭강정정식_생맥주 = new MenuProduct(1L, 1L, 생맥주.getId(), 1);
        MenuProduct 닭강정정식_닭강정 = new MenuProduct(1L, 2L, 닭강정.getId(), 2);
        //given
        List<MenuProduct> 닭강정정식_상품들 = Arrays.asList(닭강정정식_닭강정, 닭강정정식_생맥주);
        //given
        Menu menu = new Menu("닭강정정식", BigDecimal.valueOf(5000), 일식.getId(), 닭강정정식_상품들);

        //when
        final ExtractableResponse<Response> createResponse = 메뉴등록을_요청(menu);
        //then
        메뉴등록이_됨(createResponse);

        //when
        final ExtractableResponse<Response> retrieveResponse = 등록한_메뉴들을_조회();
        //then
        등록한_메뉴가_조회됨(menu, retrieveResponse);


    }

    public static ExtractableResponse<Response> 메뉴등록을_요청(Menu menu) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then()
                .log().all()
                .extract();
    }

    private void 메뉴등록이_됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header(HttpHeaders.LOCATION)).isNotEmpty();
        assertThat(createResponse.jsonPath().getString("name")).isEqualTo("닭강정정식");
        assertThat(createResponse.jsonPath().getList("menuProducts", MenuProduct.class)).hasSize(2);
    }

    private ProductResponse 상품이_등록되어_있음(ProductRequest product) {
        return ProductAcceptanceTest.상품_등록을_요청(product).as(ProductResponse.class);
    }


    private ExtractableResponse<Response> 등록한_메뉴들을_조회() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then()
                .log().all()
                .extract();
    }

    private void 등록한_메뉴가_조회됨(Menu menu, ExtractableResponse<Response> retriveResponse) {
        assertThat(retriveResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(retriveResponse.jsonPath().getList("name")).contains(menu.getName());
    }

    private MenuGroup 메뉴그룹이_등록되어있음(MenuGroupRequest menuGroupRequest) {
        return MenuGroupAcceptanceTest.메뉴그룹_등록을_요청(menuGroupRequest).as(MenuGroup.class);
    }


}
