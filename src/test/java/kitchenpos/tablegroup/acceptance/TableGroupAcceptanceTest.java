package kitchenpos.tablegroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menu.acceptance.MenuAcceptanceUtils.메뉴_등록되어_있음;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceUtils.메뉴_그룹_등록되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceUtils.주문_등록되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceUtils.주문_상태_변경_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceUtils.상품_등록되어_있음;
import static kitchenpos.ordertable.acceptance.TableAcceptanceUtils.주문_테이블_등록되어_있음;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest emptyOrderTable1;
    private OrderTableRequest emptyOrderTable2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        emptyOrderTable1 = OrderTableRequest.of(3, true);
        emptyOrderTable2 = OrderTableRequest.of(2, true);

    }

    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        OrderTableResponse orderTable1 = 주문_테이블_등록되어_있음(emptyOrderTable1).as(OrderTableResponse.class);
        OrderTableResponse orderTable2 = 주문_테이블_등록되어_있음(emptyOrderTable2).as(OrderTableResponse.class);

        TableGroupRequest groupRequest = TableGroupRequest.from(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        ExtractableResponse<Response> response = 단체_지정_요청함(groupRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("주문 테이블이 2개 이상이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createFail() {
        OrderTableResponse orderTable1 = 주문_테이블_등록되어_있음(emptyOrderTable1).as(OrderTableResponse.class);
        TableGroupRequest groupRequest = TableGroupRequest.from(Arrays.asList(orderTable1.getId()));

        ExtractableResponse<Response> response = 단체_지정_요청함(groupRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("단체 지정할 주문 테이블이 등록된 주문 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createFail2() {
        주문_테이블_등록되어_있음(emptyOrderTable1);
        주문_테이블_등록되어_있음(emptyOrderTable2);

        ExtractableResponse<Response> response = 단체_지정_요청함(TableGroupRequest.from(Arrays.asList(3L, 4L)));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @DisplayName("등록된 주문 테이블 하나라도 빈 테이블이면 단체 지정을 할 수 없다.")
    @Test
    void createFail3() {
        OrderTableResponse orderTable = 주문_테이블_등록되어_있음(emptyOrderTable1).as(OrderTableResponse.class);
        OrderTableResponse notEmptyTable = 주문_테이블_등록되어_있음(OrderTableRequest.of(2, false))
                .as(OrderTableResponse.class);

        TableGroupRequest groupRequest = TableGroupRequest.from(Arrays.asList(orderTable.getId(), notEmptyTable.getId()));
        ExtractableResponse<Response> response = 단체_지정_요청함(groupRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이미 단체 지정이 된 주문 테이블이면 단체 지정을 할 수 없다.")
    @Test
    void createFail4() {
        OrderTableResponse orderTable1 = 주문_테이블_등록되어_있음(emptyOrderTable1).as(OrderTableResponse.class);
        OrderTableResponse orderTable2 = 주문_테이블_등록되어_있음(emptyOrderTable2).as(OrderTableResponse.class);

        TableGroupRequest groupRequest = TableGroupRequest.from(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        단체_지정_등록되어_있음(groupRequest);

        ExtractableResponse<Response> response = 단체_지정_요청함(groupRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("단체 지정을 취소할 수 있다.")
    @Test
    void ungroup() {
        OrderTableResponse orderTable1 = 주문_테이블_등록되어_있음(emptyOrderTable1).as(OrderTableResponse.class);
        OrderTableResponse orderTable2 = 주문_테이블_등록되어_있음(emptyOrderTable2).as(OrderTableResponse.class);

        TableGroupRequest groupRequest = TableGroupRequest.from(Arrays.asList(orderTable1.getId(), orderTable2.getId()));
        TableGroupResponse grouped = 단체_지정_등록되어_있음(groupRequest).as(TableGroupResponse.class);

        ExtractableResponse<Response> response = 단체_지정_취소_요청함(grouped.getId());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("단체 지정된 주문 테이블들의 상태가 조리이면 단체 지정을 취소할 수 없다.")
    @Test
    void ungroupFail() {

        // Given 메뉴 그룹 등록되어 있음
        MenuGroupResponse premiumMenu = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("premiumMenu")).as(MenuGroupResponse.class);

        // And 상품 등록되어 있음
        ProductResponse honeycombo = 상품_등록되어_있음(ProductRequest.of("honeycombo", BigDecimal.valueOf(18000)))
                .as(ProductResponse.class);

        // And 메뉴 등록되어 있음
        List<MenuProductRequest> menuProducts = Arrays.asList(MenuProductRequest.of(honeycombo.getId(), 2));
        MenuResponse honeycomboChicken = 메뉴_등록되어_있음(
                MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(18000), premiumMenu.getId(), menuProducts)
        ).as(MenuResponse.class);

        // And 주문 테이블 여러개 등록되어 있음
        OrderTableResponse registerOrder1 = 주문_테이블_등록되어_있음(emptyOrderTable1).as(OrderTableResponse.class);
        OrderTableResponse registerOrder2 = 주문_테이블_등록되어_있음(emptyOrderTable2).as(OrderTableResponse.class);

        // And 단체 지정 되어 있음
        TableGroupRequest groupRequest = TableGroupRequest.from(Arrays.asList(registerOrder1.getId(), registerOrder2.getId()));
        TableGroupResponse grouped = 단체_지정_등록되어_있음(groupRequest).as(TableGroupResponse.class);

        // And 주문(조리) 등록되어 있음
        List<OrderLineItemRequest> orderItems = Arrays.asList(OrderLineItemRequest.of(honeycomboChicken.getId(), 2));
        주문_등록되어_있음(OrderRequest.of(registerOrder1.getId(), orderItems));
        주문_등록되어_있음(OrderRequest.of(registerOrder2.getId(), orderItems));

        // When 단체 지정 취소 요청함
        ExtractableResponse<Response> response = 단체_지정_취소_요청함(grouped.getId());

        // Then 단체 지정 취소 실패함
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("단체 지정된 주문 테이블들의 상태가 식사이면 단체 지정을 취소할 수 없다.")
    @Test
    void ungroupFail2() {
        // Given 메뉴 그룹 등록되어 있음
        MenuGroupResponse premiumMenu = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("premiumMenu")).as(MenuGroupResponse.class);

        // And 상품 등록되어 있음
        ProductResponse honeycombo = 상품_등록되어_있음(ProductRequest.of("honeycombo", BigDecimal.valueOf(18000)))
                .as(ProductResponse.class);

        // And 메뉴 등록되어 있음
        List<MenuProductRequest> menuProducts = Arrays.asList(MenuProductRequest.of(honeycombo.getId(), 2));
        MenuResponse honeycomboChicken = 메뉴_등록되어_있음(
                MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(18000), premiumMenu.getId(), menuProducts)
        ).as(MenuResponse.class);

        // And 주문 테이블 여러개 등록되어 있음
        OrderTableResponse registerOrder1 = 주문_테이블_등록되어_있음(emptyOrderTable1).as(OrderTableResponse.class);
        OrderTableResponse registerOrder2 = 주문_테이블_등록되어_있음(emptyOrderTable2).as(OrderTableResponse.class);

        // And 단체 지정 되어 있음
        TableGroupRequest groupRequest = TableGroupRequest.from(Arrays.asList(registerOrder1.getId(), registerOrder2.getId()));
        TableGroupResponse grouped = 단체_지정_등록되어_있음(groupRequest).as(TableGroupResponse.class);

        // And 주문(조리) 등록되어 있음
        List<OrderLineItemRequest> orderItems = Arrays.asList(OrderLineItemRequest.of(honeycomboChicken.getId(), 2));
        OrderResponse order = 주문_등록되어_있음(OrderRequest.of(registerOrder1.getId(), orderItems)).as(OrderResponse.class);
        주문_등록되어_있음(OrderRequest.of(registerOrder2.getId(), orderItems));

        // And 주문 상태(식사) 변경되어 있음
        주문_상태_변경_요청(order.getId(), OrderRequest.from(OrderStatus.MEAL));

        // When 단체 지정 취소 요청함
        ExtractableResponse<Response> response = 단체_지정_취소_요청함(grouped.getId());

        // Then 단체 지정 취소 실패함
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

}
