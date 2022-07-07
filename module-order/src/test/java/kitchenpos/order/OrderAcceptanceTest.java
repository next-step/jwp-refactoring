package kitchenpos.order;

import static kitchenpos.order.OrderAcceptanceAPI.주문_상태_변경;
import static kitchenpos.order.OrderAcceptanceAPI.주문_생성_요청;
import static kitchenpos.order.OrderAcceptanceAPI.주문_조회_요청;
import static kitchenpos.table.TableAcceptanceAPI.손님_입장;
import static kitchenpos.table.TableAcceptanceAPI.테이블_상태_변경_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Collections;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class OrderAcceptanceTest extends AcceptanceTest {

    MenuProduct 메뉴상품;
    OrderTable 주문테이블;
    OrderLineItem 주문목록;
    public static boolean 빈자리 = true;
    public static boolean 사용중 = false;

    @BeforeEach
    public void setUp() {
        super.setUp();

        메뉴상품 = new MenuProduct(1L, 1L);

        주문테이블 = 손님_입장(5, 빈자리).as(OrderTable.class);
        테이블_상태_변경_요청(주문테이블, 사용중);

        주문목록 = new OrderLineItem(1L, 1L);
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

