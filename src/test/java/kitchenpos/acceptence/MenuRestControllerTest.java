package kitchenpos.acceptence;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
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

public class MenuRestControllerTest  extends AcceptanceSupport {
    private Product 후라이드치킨;
    private Product 제로콜라;
    private MenuGroup 치킨;
    private MenuProduct 후라이드_이인분;
    private MenuProduct 제로콜라_삼인분;
    private Menu 후치콜세트;

    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드치킨 = 상품을_등록한다(new Product(1L, "후라이드치킨", BigDecimal.valueOf(3_000))).as(Product.class);
        제로콜라 = 상품을_등록한다(new Product(2L, "제로콜라", BigDecimal.valueOf(2_000))).as(Product.class);
        치킨 = 메뉴그룹을_생성한다(new MenuGroup(1L, "치킨")).as(MenuGroup.class);

        후라이드_이인분 = new MenuProduct(1L, 1L, 후라이드치킨.getId(), 2);
        제로콜라_삼인분 = new MenuProduct(1L, 1L, 제로콜라.getId(), 3);

        후치콜세트 = new Menu(1L, "후치콜세트", BigDecimal.valueOf(5_000), 치킨.getId(), Arrays.asList(제로콜라_삼인분, 후라이드_이인분));
    }

    @Test
    @DisplayName("메뉴를 생성할 수 있다")
    void menuCreate() {
        // when
        ExtractableResponse<Response> response = 메뉴를_생성한다(후치콜세트);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("메뉴 리스트를 받아 올 수 있다")
    void getMenuList() {
        // given
        후치콜세트 = 메뉴를_생성한다(후치콜세트).as(Menu.class);

        // when
        ExtractableResponse<Response> response = 메뉴_리스트를_조회해온다();

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        메뉴_목록_응답됨(response, Arrays.asList(후치콜세트.getId()));
    }

    public static ExtractableResponse<Response> 메뉴를_생성한다(Menu menu) {
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
        List<Menu> result = response.jsonPath().getList(".", Menu.class);
        List<Long> responseId = result.stream().map(Menu::getId).collect(Collectors.toList());
        assertThat(responseId).containsAll(createId);
    }
}
