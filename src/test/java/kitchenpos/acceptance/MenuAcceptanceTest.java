package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends AcceptanceTest {
    private Product 불고기;
    private Product 김치;
    private Product 공기밥;
    private MenuGroup 한식;
    private MenuProduct 불고기상품;
    private MenuProduct 김치상품;
    private MenuProduct 공기밥상품;
    private Menu 불고기정식;

    @BeforeEach
    public void setUp() {
        super.setUp();
        불고기 = 상품_생성_요청(new Product(1L, "불고기", BigDecimal.valueOf(10_000))).as(Product.class);
        김치 = 상품_생성_요청(new Product(2L, "김치", BigDecimal.valueOf(1_000))).as(Product.class);
        공기밥 = 상품_생성_요청(new Product(3L, "공기밥", BigDecimal.valueOf(1_000))).as(Product.class);
        한식 = 메뉴그룹_생성_요청(new MenuGroup(1L, "한식")).as(MenuGroup.class);

        불고기정식 = new Menu(1L, "불고기정식", BigDecimal.valueOf(12_000L), 한식, new ArrayList<>());
        불고기상품 = new MenuProduct(null, 1L, 불고기정식, 불고기);
        김치상품 = new MenuProduct(null, 1L, 불고기정식, 김치);
        공기밥상품 = new MenuProduct(null, 1L, 불고기정식, 공기밥);
        불고기정식.setMenuProducts(Arrays.asList(불고기상품, 김치상품, 공기밥상품));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(불고기정식);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findAllMenu() {
        // given
        불고기정식 = 메뉴_생성_요청(불고기정식).as(Menu.class);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response, Arrays.asList(불고기정식.getId()));
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(Menu menu) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_목록_응답됨(ExtractableResponse<Response> response, List<Long> menuIds) {
        List<Long> ids = response.jsonPath().getList(".", MenuResponse.class)
                        .stream()
                        .map(MenuResponse::getId)
                        .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(ids).containsAll(menuIds)
        );
    }
}
