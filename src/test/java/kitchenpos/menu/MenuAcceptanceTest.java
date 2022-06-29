package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ProductAcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTest extends AcceptanceTest {

    MenuGroup 한마리메뉴;
    Product 양념치킨상품;
    MenuProduct 양념치킨메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        한마리메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_생성되어_있음("추천메뉴").as(MenuGroup.class);
        양념치킨상품 = ProductAcceptanceTest.상품_생성되어_있음("양념치킨상품", new BigDecimal(18000)).as(Product.class);

        양념치킨메뉴 = new MenuProduct();
        양념치킨메뉴.setProductId(양념치킨상품.getId());
        양념치킨메뉴.setQuantity(1L);
    }

    @Test
    @DisplayName("메뉴를 생성한다")
    void 메뉴를_생성한다() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청("청양마요치킨", new BigDecimal(18000), 한마리메뉴.getId(),
                Collections.singletonList(양념치킨메뉴));

        // then
        메뉴_생성됨(response);
    }

    @Test
    @DisplayName("메뉴를 조회한다")
    void 메뉴를_조회한다() {
        // given
        메뉴_생성_요청("청양마요치킨", new BigDecimal(18000), 한마리메뉴.getId(), Collections.singletonList(양념치킨메뉴));

        // when
        ExtractableResponse<Response> response = 메뉴_조회_요청();

        // then
        assertThat(response.jsonPath().getList("name")).hasSize(1);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId,
                                                         List<MenuProduct> menuProductList) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProductList);

        return AcceptanceTest.doPost("/api/menus", menu);
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return AcceptanceTest.doGet("/api/menus");
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
