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
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuGroupDto;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.ProductDto;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
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
        MenuGroupDto 신메뉴그룹 = MenuGroupRestControllerTest.메뉴그룹_저장(MenuGroupDto.of(this.사이드메뉴)).as(MenuGroupDto.class);
        List<MenuProductDto> 메뉴_상품패키지 = 제품팩키지_생성(List.of(ProductDto.of(this.참치맛감자튀김), ProductDto.of(this.고등어맛감자튀김)));
        
        MenuDto 신매뉴 = MenuDto.of("감튀세상",  BigDecimal.valueOf(3_000), 신메뉴그룹.getId(), 메뉴_상품패키지);
        MenuDto 등록된_신메뉴 = MenuRestControllerTest.메뉴_저장요청(신매뉴).as(MenuDto.class);

        // then
        메뉴_저장됨(신메뉴그룹, 등록된_신메뉴);
    }

    @DisplayName("신규 주문테이블 생성")
    @Test
    void create_orderTable() {
        // given
        OrderTableDto 신규_주문테이블 = 신규_주문테이블_생성();

        // when
        ExtractableResponse<Response> response = TableRestControllerTest.주문테이블_저장요청(신규_주문테이블);

        // then
        신규_주문테이블_저장됨(response);
    }

    private OrderTableDto 신규_주문테이블_생성() {
        OrderTableDto 신규_주문테이블 = OrderTableDto.of(null, 0);
        return 신규_주문테이블;
    }

    private void 신규_주문테이블_저장됨(ExtractableResponse<Response> response) {
        assertAll(
            () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> Assertions.assertThat(response.as(OrderTableDto.class).getTableGroupId()).isNull(),
            () -> Assertions.assertThat(response.as(OrderTableDto.class).getNumberOfGuests()).isEqualTo(0),
            () -> Assertions.assertThat(response.as(OrderTableDto.class).isEmpty()).isEqualTo(true)
        );
    }

    private void 메뉴_저장됨(MenuGroupDto 신메뉴그룹, MenuDto 등록된_신메뉴) throws MultipleFailuresError {
        assertAll(
            () -> Assertions.assertThat(등록된_신메뉴.getName()).isEqualTo(this.신메뉴.getName()),
            () -> Assertions.assertThat(등록된_신메뉴.getPrice()).isEqualTo(BigDecimal.valueOf(신메뉴.getPrice().value())),
            () -> Assertions.assertThat(등록된_신메뉴.getMenuGroupId()).isEqualTo(신메뉴그룹.getId())
        );

        for (MenuProductDto menuproduct : 등록된_신메뉴.getMenuProducts()) {
            Assertions.assertThat(menuproduct.getMenuId()).isEqualTo(등록된_신메뉴.getId());
        }
    }

    private List<MenuProductDto> 제품팩키지_생성(List<ProductDto> products) {
        List<MenuProductDto> productPackage = new ArrayList<>();

        for (ProductDto product : products) {
            ProductDto createdProduct = ProductRestControllerTest.상품_저장요청(product).as(ProductDto.class);

            MenuProductDto menuProduct = MenuProductDto.of(null, createdProduct.getId(), 1L);
            productPackage.add(menuProduct);
        }

        return productPackage;
    }
}
