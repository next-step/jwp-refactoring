package kitchenpos.utils.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableResponse;

public class ResponseHelper {
    public static Long 공통_번호_추출(ExtractableResponse<Response> 상품_생성_요청_응답) {
        String[] locationInfo = 상품_생성_요청_응답.header("Location").split("/");
        return Long.parseLong(locationInfo[3]);
    }

    public static Long 메뉴_그룹_번호_추출(ExtractableResponse<Response> 메뉴_그룹_생성_요청_응답) {
        MenuGroupResponse 응답 = 메뉴_그룹_생성_요청_응답.as(MenuGroupResponse.class);
        return 응답.getId();
    }

    public static void 요청_실패_확인(ExtractableResponse<Response> 실패_응답) {
        assertThat(실패_응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 생성_요청_확인(ExtractableResponse<Response> 생성_응답) {
        assertThat(생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 제거_요청_확인(ExtractableResponse<Response> 제거_응답) {
        assertThat(제거_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 주문_테이블_조회_요청_응답_확인(ExtractableResponse<Response> 주문_테이블_조회_요청_응답) {
        List<OrderTableResponse> orderTableResponses = 주문_테이블_조회_요청_응답.jsonPath().getList("", OrderTableResponse.class);
        assertThat(orderTableResponses.get(0).isEmpty()).isFalse();
    }

    public static void 주문_테이블_비어있음_요청_응답_확인(ExtractableResponse<Response> 주문_테이블_비어있음_요청_응답) {
        OrderTableResponse orderTableResponse = 주문_테이블_비어있음_요청_응답.as(OrderTableResponse.class);
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    public static void 주문_테이블_고객수_변경_요청_응답_확인(ExtractableResponse<Response> 주문_테이블_고객수_변경_요청_응답, int 고객_수) {
        OrderTableResponse orderTableResponse = 주문_테이블_고객수_변경_요청_응답.as(OrderTableResponse.class);
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(고객_수);
    }

    public static void 주문_조회_요청_응답_확인(ExtractableResponse<Response> 주문_조회_요청_응답) {
        List<OrderResponse> orderResponses = 주문_조회_요청_응답.jsonPath().getList("", OrderResponse.class);
        assertThat(orderResponses.get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    public static void 주문_상태_변경_확인(ExtractableResponse<Response> 주문_상태_변경_요청_응답, OrderStatus orderStatus) {
        assertThat(주문_상태_변경_요청_응답.body().jsonPath().get("orderStatus").toString()).isEqualTo(orderStatus.name());
    }

    public static void 메뉴_그룹_조회_요청_응답_확인(ExtractableResponse<Response> 메뉴_그룹_조회_요청_응답, String 메뉴_그룹_이름) {
        List<MenuGroupResponse> menuGroupResponses = 메뉴_그룹_조회_요청_응답.jsonPath().getList("", MenuGroupResponse.class);
        assertThat(menuGroupResponses.get(0).getName()).isEqualTo(메뉴_그룹_이름);
    }

    public static Long 상품_번호_추출(ExtractableResponse<Response> 상품_생성_요청_응답) {
        String[] locationInfo = 상품_생성_요청_응답.header("Location").split("/");
        return Long.parseLong(locationInfo[3]);
    }

    public static void 메뉴_조회_요청_응답_확인(ExtractableResponse<Response> 메뉴_조회_요청_응답, String 메뉴_이름, Long 상품_수량, int 상품_가격, String 상품_이름, String 메뉴_그룹_이름) {
        List<MenuResponse> menuResponses = 메뉴_조회_요청_응답.jsonPath().getList("", MenuResponse.class);
        MenuResponse menuResponse = menuResponses.get(0);
        assertThat(menuResponse.getName()).isEqualTo(메뉴_이름);
        assertThat(menuResponse.getMenuProductResponses().get(0).getQuantity()).isEqualTo(상품_수량);
        assertThat(menuResponse.getMenuProductResponses().get(0).getProductResponse().getPrice()).isEqualTo(상품_가격);
        assertThat(menuResponse.getMenuProductResponses().get(0).getProductResponse().getName()).isEqualTo(상품_이름);
        assertThat(menuResponse.getMenuGroupResponse().getName()).isEqualTo(메뉴_그룹_이름);
    }
}
