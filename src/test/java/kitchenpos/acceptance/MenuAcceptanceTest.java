package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴")
public class MenuAcceptanceTest extends AcceptanceTest {

    private ProductResponse 생맥주;
    private ProductResponse 닭강정;
    private MenuGroupResponse 일식;
    private ExtractableResponse<Response> 닭강정정식;

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
    @DisplayName("메뉴를 등록 관리")
    @TestFactory
    Stream<DynamicTest> menuCrateManage() {
        return Stream.of(
                dynamicTest("메뉴를 등록 한다", () -> {
                    MenuProductRequest 닭강정정식_생맥주 = new MenuProductRequest(생맥주.getId(), 1);
                    MenuProductRequest 닭강정정식_닭강정 = new MenuProductRequest(닭강정.getId(), 2);
                    //given
                    List<MenuProductRequest> 닭강정정식_상품들 = Arrays.asList(닭강정정식_닭강정, 닭강정정식_생맥주);
                    //given
                    MenuRequest menu = new MenuRequest("닭강정정식", BigDecimal.valueOf(5000), 일식.getId(), 닭강정정식_상품들);

                    //when
                    닭강정정식 = 메뉴등록을_요청(menu);
                    메뉴등록이_됨(닭강정정식);

                })
                , dynamicTest("등록한 메뉴를 조회", () -> {
                    MenuResponse 닭강정_정식 = 닭강정정식.as(MenuResponse.class);
                    //when
                    final ExtractableResponse<Response> retrieveResponse = 메뉴_조회(닭강정_정식.getId());
                    //then
                    메뉴가_조회됨(닭강정_정식.getName(), retrieveResponse);

                }),dynamicTest("메뉴를 한개 더 등록을 한다", () -> {
                    //given
                    MenuProductRequest 닭강정_단품 = new MenuProductRequest(닭강정.getId(), 1);
                    MenuRequest menu = new MenuRequest("닭강정정식", BigDecimal.valueOf(1000), 일식.getId(), Collections.singletonList(닭강정_단품));

                    //when
                    final ExtractableResponse<Response> createResponse = 메뉴등록을_요청(menu);
                    //then
                    메뉴등록이_됨(createResponse);
                })
                , dynamicTest("등록한 메뉴들을 조회",() -> {
                    final ExtractableResponse<Response> retrieveResponse = 등록한_메뉴들을_조회();
                    등록한_메뉴들이_조회됨(retrieveResponse);
                })
        );
    }

    private void 등록한_메뉴들이_조회됨(ExtractableResponse<Response> retrieveResponse) {
        assertThat(retrieveResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(retrieveResponse.jsonPath().getList(".")).hasSize(2);
    }


    public static ExtractableResponse<Response> 메뉴등록을_요청(MenuRequest menuRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then()
                .log().all()
                .extract();
    }

    private void 메뉴등록이_됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(createResponse.header(HttpHeaders.LOCATION)).isNotEmpty();
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

    private MenuGroupResponse 메뉴그룹이_등록되어있음(MenuGroupRequest menuGroupRequest) {
        return MenuGroupAcceptanceTest.메뉴그룹_등록을_요청(menuGroupRequest).as(MenuGroupResponse.class);
    }

    private ExtractableResponse<Response> 메뉴_조회(Long menuId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("menuId", menuId)
                .when().get("/api/menus/{menuId}")
                .then()
                .log().all()
                .extract();
    }


    private void 메뉴가_조회됨(String menuName, ExtractableResponse<Response> retrievedResponse) {
        assertThat(retrievedResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(retrievedResponse.as(MenuResponse.class).getName()).isEqualTo(menuName);
    }



}
