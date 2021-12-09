package kitchenpos.acceptance.manage;

import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.fixture.Fixture;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.presentation.MenuGroupTest;
import kitchenpos.presentation.MenuTest;
import kitchenpos.presentation.ProductTest;
import kitchenpos.presentation.TableTest;
import kitchenpos.testassistance.config.TestConfig;

public class KitchenposManageAcceptanceTest extends TestConfig {
    @DisplayName("새로운 유형의 메뉴가 추가된다.")
    @Test
    void addMenu_newGroup() {
        // when
        MenuGroup 신메뉴그룹 = MenuGroupTest.메뉴그룹_저장(Fixture.사이드메뉴).as(MenuGroup.class);
        List<MenuProduct> 메뉴_상품패키지 = 제품팩키지_생성(List.of(Fixture.참치맛감자튀김, Fixture.고등어맛감자튀김));
        Menu 신매뉴 = 신메뉴_생성(신메뉴그룹, 메뉴_상품패키지);
        Menu 등록된_신메뉴 = MenuTest.메뉴_저장요청(신매뉴).as(Menu.class);

        // then
        메뉴_저장됨(신메뉴그룹, 등록된_신메뉴);    
    }

    @DisplayName("신규 주문테이블 생성")
    @Test
    void create_orderTable() {
        // given
        OrderTable 신규_주문테이블 = 신규_주문테이블_생성();

        // when
        ExtractableResponse<Response> response = TableTest.주문테이블_저장요청(신규_주문테이블);

        // then
        신규_주문테이블_저장됨(response);
    }

    private OrderTable 신규_주문테이블_생성() {
        OrderTable 신규_주문테이블 = new OrderTable();
        신규_주문테이블.setTableGroupId(null);
        신규_주문테이블.setNumberOfGuests(0);
        신규_주문테이블.setEmpty(true);
        return 신규_주문테이블;
    }

    private void 신규_주문테이블_저장됨(ExtractableResponse<Response> response) { 
        assertAll(
            () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> Assertions.assertThat(response.as(OrderTable.class).getTableGroupId()).isNull(),
            () -> Assertions.assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(0),
            () -> Assertions.assertThat(response.as(OrderTable.class).isEmpty()).isEqualTo(true)
        );
    }

    private Menu 신메뉴_생성(MenuGroup 신메뉴그룹, List<MenuProduct> 메뉴_상품패키지) {
        Menu 신매뉴 = Fixture.신메뉴;
        신매뉴.setMenuGroupId(신메뉴그룹.getId());
        신매뉴.setMenuProducts(메뉴_상품패키지);
        return 신매뉴;
    }

    private void 메뉴_저장됨(MenuGroup 신메뉴그룹, Menu 등록된_신메뉴) throws MultipleFailuresError {
        assertAll(
            () -> Assertions.assertThat(등록된_신메뉴.getName()).isEqualTo(Fixture.신메뉴.getName()),
            () -> Assertions.assertThat(등록된_신메뉴.getPrice()).isEqualTo(Fixture.신메뉴.getPrice()),
            () -> Assertions.assertThat(등록된_신메뉴.getMenuGroupId()).isEqualTo(신메뉴그룹.getId())
        );

        for (MenuProduct menuproduct : 등록된_신메뉴.getMenuProducts()) {
            Assertions.assertThat(menuproduct.getMenuId()).isEqualTo(등록된_신메뉴.getId());
        }
    }

    private List<MenuProduct> 제품팩키지_생성(List<Product> products) {
        List<MenuProduct> productPackage = new ArrayList<>();

        for (Product product : products) {
            Product createdProduct = ProductTest.상품_저장요청(product).as(Product.class);
            
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setProductId(createdProduct.getId());
            menuProduct.setQuantity(1L);
            productPackage.add(menuProduct);
        }

        return productPackage;
    }
}
