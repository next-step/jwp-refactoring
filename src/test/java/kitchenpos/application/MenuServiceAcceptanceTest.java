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

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("메뉴 테스트")
class MenuServiceAcceptanceTest extends AcceptanceTest {

    MenuGroup createdMenuGroup;
    Product createdProduct;
    Menu createdMenu;

    @BeforeEach
    public void setUp() {
        super.setUp();

        ExtractableResponse<Response> createdResponse;

        Product product = new Product();
        product.setName("후라이드치킨");
        product.setPrice(BigDecimal.valueOf(18000));

        createdResponse = ProductFactory.상품_생성_요청(product);
        createdProduct = ProductServiceAcceptanceTest.상품이_생성됨(createdResponse);

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("치킨");

        createdResponse = MenuGroupFactory.메뉴그룹_생성_요청(menuGroup);
        createdMenuGroup = MenuGroupServiceAcceptanceTest.메뉴그룹이_생성됨(createdResponse);

    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void createTest() {

        Menu menu = new Menu();
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(18000));
        menu.setMenuGroupId(createdMenuGroup.getId());


        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(createdProduct.getId());
        menuProduct.setQuantity(10L);

        menu.setMenuProducts(Arrays.asList(menuProduct));

        ExtractableResponse<Response> createResponse = MenuFactory.메뉴_생성_요청(menu);
        createdMenu = 메뉴가_생성됨(createResponse);
    }

    @DisplayName("메뉴를 조회한다")
    @Test
    void getListTest() {

        Menu menu = new Menu();
        menu.setName("후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(18000));
        menu.setMenuGroupId(createdMenuGroup.getId());

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(menu.getId());
        menuProduct.setProductId(createdProduct.getId());
        menuProduct.setQuantity(10L);

        menu.setMenuProducts(Arrays.asList(menuProduct));

        ExtractableResponse<Response> createResponse = MenuFactory.메뉴_생성_요청(menu);
        createdMenu = 메뉴가_생성됨(createResponse);

        ExtractableResponse<Response> getResponse = MenuFactory.메뉴_조회_요청();
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static Menu 메뉴가_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(Menu.class);
    }

}
