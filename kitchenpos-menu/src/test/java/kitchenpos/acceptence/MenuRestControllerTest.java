package kitchenpos.acceptence;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductPrice;
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

import static kitchenpos.acceptence.MenuGroupRestControllerTest.메뉴그룹을_생성한다;
import static kitchenpos.acceptence.ProductRestControllerTest.상품을_등록한다;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuRestControllerTest extends AcceptanceSupport {
    private MenuGroupResponse 치킨;
    private MenuProduct 후라이드_이인분;
    private MenuProduct 제로콜라_삼인분;
    private Menu 후치콜세트;
    private MenuRequest 메뉴요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ProductResponse responseA = 상품을_등록한다(new ProductRequest("치킨", BigDecimal.valueOf(10_000))).as(ProductResponse.class);
        ProductResponse responseB = 상품을_등록한다(new ProductRequest("제로콜라", BigDecimal.valueOf(1_000))).as(ProductResponse.class);


        치킨 = 메뉴그룹을_생성한다(new MenuGroupRequest("치킨")).as(MenuGroupResponse.class);

        MenuProducts menuProducts = new MenuProducts(Arrays.asList(new MenuProduct(responseA.getId(), 2L), new MenuProduct(responseB.getId(), 2L)));

        후치콜세트 = new Menu("후치콜세트", new MenuPrice(BigDecimal.valueOf(10_000)), 치킨.getId(), menuProducts);

        후라이드_이인분 = new MenuProduct(1L, new Product(new ProductPrice(BigDecimal.valueOf(3_000)), "후라이드치킨").getId(), 2L);
        제로콜라_삼인분 = new MenuProduct(2L, new Product(new ProductPrice(BigDecimal.valueOf(2_000)), "제로콜라").getId(), 3L);

        MenuProductRequest productRequestA = new MenuProductRequest(후라이드_이인분.getSeq(), 후라이드_이인분.getQuantity());
        MenuProductRequest productRequestB = new MenuProductRequest(제로콜라_삼인분.getSeq(), 제로콜라_삼인분.getQuantity());

        메뉴요청 = new MenuRequest(후치콜세트.getName(), 후치콜세트.getPrice().getPrice(), 치킨.getId(), Arrays.asList(productRequestA, productRequestB));
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다")
    void menuCreate() {
        // when
        ExtractableResponse<Response> response = 메뉴를_생성한다(메뉴요청);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("메뉴 리스트를 받아 올 수 있다")
    void getMenuList() {
        // given
        MenuResponse 후치콜세트_응답 = 메뉴를_생성한다(메뉴요청).as(MenuResponse.class);

        // when
        ExtractableResponse<Response> response = 메뉴_리스트를_조회해온다();

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        메뉴_목록_응답됨(response, Arrays.asList(후치콜세트_응답.getId()));
    }

    public static ExtractableResponse<Response> 메뉴를_생성한다(MenuRequest menu) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_리스트를_조회해온다() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private void 상태값을_비교한다(int statusCode, HttpStatus created) {
        assertThat(statusCode).isEqualTo(created.value());
    }

    private void 메뉴_목록_응답됨(ExtractableResponse<Response> response, List<Long> createId) {
        List<MenuResponse> result = response.jsonPath().getList(".", MenuResponse.class);
        List<Long> responseId = result.stream().map(MenuResponse::getId).collect(Collectors.toList());
        assertThat(responseId).containsAll(createId);
    }
}
