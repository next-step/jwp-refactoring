package kitchenpos.order;

import static kitchenpos.menu.MenuAcceptanceAPI.메뉴_그룹_생성_요청;
import static kitchenpos.menu.MenuAcceptanceAPI.메뉴_생성_요청;
import static kitchenpos.order.OrderAcceptanceAPI.주문_상태_변경;
import static kitchenpos.order.OrderAcceptanceAPI.주문_생성_요청;
import static kitchenpos.order.OrderAcceptanceAPI.주문_조회_요청;
import static kitchenpos.product.ProductAcceptanceAPI.상품_생성_요청;
import static kitchenpos.table.TableAcceptanceAPI.손님_입장;
import static kitchenpos.table.TableAcceptanceAPI.테이블_상태_변경_요청;
import static kitchenpos.table.TableAcceptanceTest.빈자리;
import static kitchenpos.table.TableAcceptanceTest.사용중;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderAcceptanceTest extends AcceptanceTest {

    Product 상품;
    MenuGroup 메뉴그룹;
    MenuProduct 메뉴상품;
    MenuResponse 양념치킨;
    OrderTable 주문테이블;
    OrderLineItem 주문목록;

    @BeforeEach
    public void setUp() {
        super.setUp();

        상품 = 상품_생성_요청("양념치킨", new BigDecimal(18000)).as(Product.class);

        메뉴그룹 = 메뉴_그룹_생성_요청("추천메뉴").as(MenuGroup.class);

        메뉴상품 = new MenuProduct(상품, 1L);

        양념치킨 = 메뉴_생성_요청("양념치킨", new BigDecimal(18000), 메뉴그룹.getId(), 메뉴상품).as(MenuResponse.class);

        주문테이블 = 손님_입장(5, 빈자리).as(OrderTable.class);
        테이블_상태_변경_요청(주문테이블, 사용중);

        주문목록 = new OrderLineItem(양념치킨.getId(), 1L);
    }

    @Test
    @DisplayName("주문을 요청한다")
    void 주문을_요청한다() {
        // when
        ExtractableResponse<Response> response = 주문_생성_요청(주문테이블.getId(), Collections.singletonList(주문목록));

        // then
        주문_생성됨(response);
    }

    @Test
    @DisplayName("주문 리스트를 조회한다")
    void 주문_리스트를_조회한다() {
        // given
        주문_생성_요청(주문테이블.getId(), Collections.singletonList(주문목록));

        // when
        ExtractableResponse<Response> response = 주문_조회_요청();

        // then
        assertThat(response.jsonPath().getList("id")).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태를 변경한다")
    void 주문_상태를_변경한다() {
        // given
        OrderResponse 주문 = 주문_생성_요청(주문테이블.getId(), Collections.singletonList(주문목록)).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_상태_변경(주문.getId(), OrderStatus.MEAL);

        // then
        assertThat(response.jsonPath().getString("orderStatus")).isEqualTo(OrderStatus.MEAL.name());
    }

    public static void 주문_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}

