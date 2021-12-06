package kitchenpos.application;


import kitchenpos.AcceptanceTest;
import kitchenpos.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.http.HttpStatus;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("메뉴 테스트")
class MenuServiceTest extends AcceptanceTest {

    MenuGroup createdMenuGroup;
    Product createdProduct;
    Menu createdMenu;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdResponse;

        Product product = new Product("후라이드치킨", BigDecimal.valueOf(18000));
        createdResponse = ProductFactory.상품_생성_요청(product);
        createdProduct = ProductServiceTest.상품이_생성됨(createdResponse);

        MenuGroup menuGroup = new MenuGroup(1L, "치킨");
        createdResponse = MenuGroupFactory.메뉴그룹_생성_요청(menuGroup);
        createdMenuGroup = MenuGroupServiceTest.메뉴그룹이_생성됨(createdResponse);

    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void createTest() {

        Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(18000), createdMenuGroup.getId());

        MenuProduct menuProduct = new MenuProduct(menu.getId(), createdProduct.getId(), 10L);
        menu.addMenuProduct(menuProduct);

        ExtractableResponse<Response> createResponse = MenuFactory.메뉴_생성_요청(menu);
        createdMenu = 메뉴가_생성됨(createResponse);
    }

    @DisplayName("메뉴를 조회한다")
    @Test
    void getListTest() {

        Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(18000), createdMenuGroup.getId());

        MenuProduct menuProduct = new MenuProduct(menu.getId(), createdProduct.getId(), 10L);
        menu.addMenuProduct(menuProduct);

        ExtractableResponse<Response> createResponse = MenuFactory.메뉴_생성_요청(menu);
        createdMenu = 메뉴가_생성됨(createResponse);

        ExtractableResponse<Response> getResponse = MenuFactory.메뉴_조회_요청();
        List<Menu> menus = Arrays.asList(getResponse.as(Menu[].class));
        assertThat(menus).contains(createdMenu);
    }

    public static Menu 메뉴가_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(Menu.class);
    }

}
