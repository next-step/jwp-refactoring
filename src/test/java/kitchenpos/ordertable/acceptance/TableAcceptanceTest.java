package kitchenpos.ordertable.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.menu.acceptance.MenuAcceptanceUtils.메뉴_등록되어_있음;
import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceUtils.메뉴_그룹_등록되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceUtils.주문_등록되어_있음;
import static kitchenpos.order.acceptance.OrderAcceptanceUtils.주문_상태_변경_요청;
import static kitchenpos.ordertable.acceptance.TableAcceptanceUtils.*;
import static kitchenpos.product.acceptance.ProductAcceptanceUtils.상품_등록되어_있음;
import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceUtils.단체_지정_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("주문 테이블 관련 인수 테스트")
class TableAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest notEmptyOrderTable1;
    private OrderTableRequest notEmptyOrderTable2;
    private OrderTableRequest emptyOrderTable1;
    private OrderTableRequest emptyOrderTable2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        notEmptyOrderTable1 = OrderTableRequest.of(2, false);
        notEmptyOrderTable2 = OrderTableRequest.of(3, false);

        emptyOrderTable1 = OrderTableRequest.of(2, true);
        emptyOrderTable2 = OrderTableRequest.of(2, true);

    }


    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void create() {
        ExtractableResponse<Response> response = 주문_테이블_생성_요청(notEmptyOrderTable1);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    @DisplayName("주문 태이블 목록을 조회한다.")
    @Test
    void list() {
        ExtractableResponse<Response> createResponse1 = 주문_테이블_등록되어_있음(notEmptyOrderTable1);
        ExtractableResponse<Response> createResponse2 = 주문_테이블_등록되어_있음(notEmptyOrderTable2);

        ExtractableResponse<Response> listResponse = 주문_테이블_목록_조회_요청();

        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<OrderTableResponse> orderTables = listResponse.jsonPath().getList(".", OrderTableResponse.class);
        List<OrderTableResponse> createdOrderTables = Arrays.asList(createResponse1, createResponse2).stream()
                .map(it -> it.as(OrderTableResponse.class))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(orderTables).hasSize(2),
                () -> assertThat(orderTables).containsAll(createdOrderTables)
        );
    }


    @DisplayName("등록되지 않은 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFail() {
        OrderTableRequest request = OrderTableRequest.of(3, false);
        ExtractableResponse<Response> response = 주문_테이블_빈_상태_변경_요청(1L, request);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("단체 지정된 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFail2() {
        OrderTableResponse registeredEmptyOrderTable1 = 주문_테이블_등록되어_있음(emptyOrderTable1).as(OrderTableResponse.class);
        OrderTableResponse registeredEmptyOrderTable2 = 주문_테이블_등록되어_있음(emptyOrderTable2).as(OrderTableResponse.class);

        List<OrderTableResponse> orderItems = Arrays.asList(registeredEmptyOrderTable1, registeredEmptyOrderTable2);
        단체_지정_등록되어_있음(
                TableGroupRequest.from(orderItems.stream()
                        .map(OrderTableResponse::getId)
                        .collect(Collectors.toList()))
        );

        OrderTableRequest changeOrderTable =
                OrderTableRequest.of(registeredEmptyOrderTable1.getNumberOfGuests(), !registeredEmptyOrderTable1.isEmpty());
        ExtractableResponse<Response> response = 주문_테이블_빈_상태_변경_요청(registeredEmptyOrderTable1.getId(), changeOrderTable);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("주문 상태가 조리이면 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFail3() {
        // Given 메뉴 그룹 등록되어 있음
        MenuGroupResponse premiumMenu = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("premiumMenu")).as(MenuGroupResponse.class);

        // And 상품 등록되어 있음
        ProductResponse honeycombo = 상품_등록되어_있음(ProductRequest.of("honeycombo", BigDecimal.valueOf(18000)))
                .as(ProductResponse.class);

        // And 메뉴 등록되어 있음
        List<MenuProductRequest> menuItems = Arrays.asList(MenuProductRequest.of(honeycombo.getId(), 2));
        MenuResponse honeycombo치킨 = 메뉴_등록되어_있음(
                MenuRequest.of("honeycombo치킨", BigDecimal.valueOf(18000), premiumMenu.getId(), menuItems)
        ).as(MenuResponse.class);

        // And 주문 테이블 등록되어 있음
        OrderTableResponse registeredOrderTable = 주문_테이블_등록되어_있음(notEmptyOrderTable1).as(OrderTableResponse.class);

        // And 주문(조리) 등록되어 있음
        List<OrderLineItemRequest> orderItems = Arrays.asList(OrderLineItemRequest.of(honeycombo치킨.getId(), 2));
        주문_등록되어_있음(OrderRequest.of(registeredOrderTable.getId(), orderItems));

        // When 주문 테이블 빈 상태 변경 요청
        OrderTableRequest changeOrderTable =
                OrderTableRequest.of(registeredOrderTable.getNumberOfGuests(), !registeredOrderTable.isEmpty());
        ExtractableResponse<Response> response = 주문_테이블_빈_상태_변경_요청(registeredOrderTable.getId(), changeOrderTable);

        // Then 빈 상태 변경 요청 실패됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("주문 상태가 식사이면 주문 테이블의 빈 상태를 변경할 수 없다.")
    @Test
    void changeEmptyFail4() {
        // Given 메뉴 그룹 등록되어 있음
        MenuGroupResponse premiumMenu = 메뉴_그룹_등록되어_있음(MenuGroupRequest.from("premiumMenu")).as(MenuGroupResponse.class);

        // And 상품 등록되어 있음
        ProductResponse honeycombo = 상품_등록되어_있음(ProductRequest.of("honeycombo", BigDecimal.valueOf(18000)))
                .as(ProductResponse.class);

        // And 메뉴 등록되어 있음
        List<MenuProductRequest> menuItems = Arrays.asList(MenuProductRequest.of(honeycombo.getId(), 2));
        MenuResponse honeycomboChicken = 메뉴_등록되어_있음(
                MenuRequest.of("honeycomboChicken", BigDecimal.valueOf(18000), premiumMenu.getId(), menuItems)
        ).as(MenuResponse.class);

        // And 주문 테이블 등록되어 있음
        OrderTableResponse registeredOrderTable = 주문_테이블_등록되어_있음(notEmptyOrderTable1).as(OrderTableResponse.class);

        // And 주문 등록되어 있음
        List<OrderLineItemRequest> orderItems = Arrays.asList(OrderLineItemRequest.of(honeycomboChicken.getId(), 2));
        OrderResponse order = 주문_등록되어_있음(OrderRequest.of(registeredOrderTable.getId(), orderItems)).as(OrderResponse.class);

        // And 주문 상태(식사) 변경되어 있음
        주문_상태_변경_요청(order.getId(), OrderRequest.from(OrderStatus.MEAL));

        // When 주문 테이블 빈 상태 변경 요청
        OrderTableRequest changeOrderTable =
                OrderTableRequest.of(registeredOrderTable.getNumberOfGuests(), !registeredOrderTable.isEmpty());
        ExtractableResponse<Response> response = 주문_테이블_빈_상태_변경_요청(registeredOrderTable.getId(), changeOrderTable);

        // Then 빈 상태 변경 요청 실패됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("주문 테이블의 빈 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTableResponse ordertable = 주문_테이블_등록되어_있음(notEmptyOrderTable1).as(OrderTableResponse.class);

        OrderTableRequest changeOrderTable = OrderTableRequest.of(ordertable.getNumberOfGuests(), !ordertable.isEmpty());
        ExtractableResponse<Response> response = 주문_테이블_빈_상태_변경_요청(ordertable.getId(), changeOrderTable);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(OrderTableResponse.class).isEmpty()).isEqualTo(changeOrderTable.isEmpty())
        );
    }


    @DisplayName("방문한 손님 수가 0보다 작은경우 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFail() {
        OrderTableRequest 방문한_손님_수가_0보다_작은_주문_테이블 = OrderTableRequest.of(-1, false);
        ExtractableResponse<Response> response =
                주문_테이블_방문한_손님_수_변경_요청(1L, 방문한_손님_수가_0보다_작은_주문_테이블);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("등록되지 않은 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFail2() {
        OrderTableRequest notregisteredOrerTable = OrderTableRequest.of(10, false);
        ExtractableResponse<Response> response = 주문_테이블_방문한_손님_수_변경_요청(1L, notregisteredOrerTable);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("등록된 주문 테이블이 빈 상태이면 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsFail3() {
        OrderTableResponse emptyOrderTable =
                주문_테이블_등록되어_있음(OrderTableRequest.of(0, true)).as(OrderTableResponse.class);

        OrderTableRequest changeOrderTable = OrderTableRequest.of(10, false);
        ExtractableResponse<Response> response =
                주문_테이블_방문한_손님_수_변경_요청(emptyOrderTable.getId(), changeOrderTable);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }


    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTableResponse orderTable = 주문_테이블_등록되어_있음(notEmptyOrderTable1).as(OrderTableResponse.class);

        OrderTableRequest changeOrderTable = OrderTableRequest.of(10, orderTable.isEmpty());
        ExtractableResponse<Response> response = 주문_테이블_방문한_손님_수_변경_요청(orderTable.getId(), changeOrderTable);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.as(OrderTableResponse.class).getNumberOfGuests()).isEqualTo(changeOrderTable.getNumberOfGuests())
        );
    }
}
