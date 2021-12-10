package kitchenpos.acceptance.manage;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.presentation.MenuGroupRestControllerTest;
import kitchenpos.presentation.MenuRestControllerTest;
import kitchenpos.presentation.ProductRestControllerTest;
import kitchenpos.presentation.TableRestControllerTest;
import kitchenpos.testassistance.config.TestConfig;

public class KitchenposManageAcceptanceTest extends TestConfig {
    private MenuGroup 사이드메뉴;

    private Product 참치맛감자튀김;
    private Product 고등어맛감자튀김;

    private Menu 신메뉴;

    @BeforeEach

    public void setUp() {
        super.setUp();

        사이드메뉴 = MenuGroup.of("사이드메뉴");

        참치맛감자튀김 = Product.of("참치맛감자튀김", Price.of(2_000));
        고등어맛감자튀김 =  Product.of("고등어맛감자튀김", Price.of(2_000));

        신메뉴 = Menu.of("감튀세상", Price.of(3_000), null, null);
    }

    @DisplayName("새로운 유형의 메뉴가 추가된다.")
    @Test
    void addMenu_newGroup() {
        // when
        MenuGroup 신메뉴그룹 = MenuGroupRestControllerTest.메뉴그룹_저장(this.사이드메뉴).as(MenuGroup.class);
        List<MenuProduct> 메뉴_상품패키지 = 제품팩키지_생성(List.of(this.참치맛감자튀김, this.고등어맛감자튀김));
        Menu 신매뉴 = 신메뉴_생성(신메뉴그룹, 메뉴_상품패키지);
        Menu 등록된_신메뉴 = MenuRestControllerTest.메뉴_저장요청(신매뉴).as(Menu.class);

        // then
        메뉴_저장됨(신메뉴그룹, 등록된_신메뉴);
    }

    @DisplayName("신규 주문테이블 생성")
    @Test
    void create_orderTable() {
        // given
        OrderTable 신규_주문테이블 = 신규_주문테이블_생성();

        // when
        ExtractableResponse<Response> response = TableRestControllerTest.주문테이블_저장요청(신규_주문테이블);

        // then
        신규_주문테이블_저장됨(response);
    }

    private OrderTable 신규_주문테이블_생성() {
        OrderTable 신규_주문테이블 = OrderTable.of(null, 0);
        return 신규_주문테이블;
    }

    private void 신규_주문테이블_저장됨(ExtractableResponse<Response> response) {
        assertAll(
            () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> Assertions.assertThat(response.as(OrderTable.class).getTableGroup().getId()).isNull(),
            () -> Assertions.assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(0),
            () -> Assertions.assertThat(response.as(OrderTable.class).isEmpty()).isEqualTo(true)
        );
    }

    private Menu 신메뉴_생성(MenuGroup 신메뉴그룹, List<MenuProduct> 메뉴_상품패키지) {
        Menu 신매뉴 = this.신메뉴;
        신매뉴.chnageMenuGroup(신메뉴그룹);
        신매뉴.changeMenuProducts(메뉴_상품패키지);
        return 신매뉴;
    }

    private void 메뉴_저장됨(MenuGroup 신메뉴그룹, Menu 등록된_신메뉴) throws MultipleFailuresError {
        assertAll(
            () -> Assertions.assertThat(등록된_신메뉴.getName()).isEqualTo(this.신메뉴.getName()),
            () -> Assertions.assertThat(등록된_신메뉴.getPrice()).isEqualTo(this.신메뉴.getPrice()),
            () -> Assertions.assertThat(등록된_신메뉴.getMenuGroup().getId()).isEqualTo(신메뉴그룹.getId())
        );

        for (MenuProduct menuproduct : 등록된_신메뉴.getMenuProducts()) {
            Assertions.assertThat(menuproduct.getMenu().getId()).isEqualTo(등록된_신메뉴.getId());
        }
    }

    private List<MenuProduct> 제품팩키지_생성(List<Product> products) {
        List<MenuProduct> productPackage = new ArrayList<>();

        for (Product product : products) {
            Product createdProduct = ProductRestControllerTest.상품_저장요청(product).as(Product.class);

            MenuProduct menuProduct = MenuProduct.of(null, createdProduct, 1L);
            productPackage.add(menuProduct);
        }

        return productPackage;
    }
}
