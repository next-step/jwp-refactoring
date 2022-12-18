package kitchenpos.tablegroup.acceptance;

import static kitchenpos.ordertable.acceptance.TableRestAssured.주문_테이블_등록되어_있음;
import static kitchenpos.tablegroup.acceptance.TableGroupRestAssured.단체_지정_등록되어_있음;
import static kitchenpos.tablegroup.acceptance.TableGroupRestAssured.단체_지정_요청함;
import static kitchenpos.tablegroup.acceptance.TableGroupRestAssured.단체_지정_취소_요청함;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.acceptance.MenuRestAssured;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.acceptance.MenuGroupRestAssured;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.acceptance.OrderRestAssured;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.acceptance.ProductRestAssured;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("단체 지정 관련 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableRequest 비어있는_주문_테이블1;
    private OrderTableRequest 비어있는_주문_테이블2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        비어있는_주문_테이블1 = OrderTableRequest.of(3, true);
        비어있는_주문_테이블2 = OrderTableRequest.of(2, true);

    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * When 주문 테이블 2개 이상을 단체 지정 요청함
     * Then 단체 지정이됨
     */
    @DisplayName("단체 지정을 할 수 있다.")
    @Test
    void create() {
        OrderTableResponse 주문_테이블1 = 주문_테이블_등록되어_있음(비어있는_주문_테이블1).as(OrderTableResponse.class);
        OrderTableResponse 주문_테이블2 = 주문_테이블_등록되어_있음(비어있는_주문_테이블2).as(OrderTableResponse.class);

        TableGroupRequest 단체지정요청 = TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()));
        ExtractableResponse<Response> response = 단체_지정_요청함(단체지정요청);

        단체_지정이됨(response);
    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * When 주문 테이블 2개 미만을 단체 지정 요청함
     * Then 단체 지정 실패함
     */
    @DisplayName("주문 테이블이 2개 이상이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createFail() {
        OrderTableResponse 주문_테이블1 = 주문_테이블_등록되어_있음(비어있는_주문_테이블1).as(OrderTableResponse.class);
        TableGroupRequest 단체지정요청 = TableGroupRequest.from(Arrays.asList(주문_테이블1.getId()));

        ExtractableResponse<Response> response = 단체_지정_요청함(단체지정요청);

        단체_지정_실패함(response);
    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * When 등록되지 않은 주문 테이블로 단체 지정 요청함
     * Then 단체 지정 실패함
     */
    @DisplayName("단체 지정할 주문 테이블이 등록된 주문 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void createFail2() {
        주문_테이블_등록되어_있음(비어있는_주문_테이블1);
        주문_테이블_등록되어_있음(비어있는_주문_테이블2);

        ExtractableResponse<Response> response = 단체_지정_요청함(TableGroupRequest.from(Arrays.asList(3L, 4L)));

        단체_지정_실패함(response);
    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * When 비어있지 않은 주문 테이블로 단체 지정 요청함
     * Then 단체 지정 실패함
     */
    @DisplayName("등록된 주문 테이블 하나라도 빈 테이블이면 단체 지정을 할 수 없다.")
    @Test
    void createFail3() {
        OrderTableResponse 주문_테이블 = 주문_테이블_등록되어_있음(비어있는_주문_테이블1).as(OrderTableResponse.class);
        OrderTableResponse 비어있지_않은_주문_테이블 = 주문_테이블_등록되어_있음(OrderTableRequest.of(2, false))
                .as(OrderTableResponse.class);

        TableGroupRequest 단체지정요청 = TableGroupRequest.from(Arrays.asList(주문_테이블.getId(), 비어있지_않은_주문_테이블.getId()));
        ExtractableResponse<Response> response = 단체_지정_요청함(단체지정요청);

        단체_지정_실패함(response);
    }

    /**
     * Given 주문 테이블이 여러개 등록되어 있음
     * And 단체 지정 등록되어 있음
     * When 이미 단체 지정된 주문 테이블로 단체 지정 요청함
     * Then 단체 지정 실패함
     */
    @DisplayName("이미 단체 지정이 된 주문 테이블이면 단체 지정을 할 수 없다.")
    @Test
    void createFail4() {
        OrderTableResponse 주문_테이블1 = 주문_테이블_등록되어_있음(비어있는_주문_테이블1).as(OrderTableResponse.class);
        OrderTableResponse 주문_테이블2 = 주문_테이블_등록되어_있음(비어있는_주문_테이블2).as(OrderTableResponse.class);

        TableGroupRequest 단체지정 = TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()));
        단체_지정_등록되어_있음(단체지정);

        ExtractableResponse<Response> response = 단체_지정_요청함(단체지정);

        단체_지정_실패함(response);
    }

    /**
     * Given 주문 테이블 여러개 등록되어 있음
     * And 단체 지정 등록되어 있음
     * When 단체 지정 취소 요청함
     * Then 단체 지정 취소됨
     */
    @DisplayName("단체 지정을 취소할 수 있다.")
    @Test
    void ungroup() {
        OrderTableResponse 주문_테이블1 = 주문_테이블_등록되어_있음(비어있는_주문_테이블1).as(OrderTableResponse.class);
        OrderTableResponse 주문_테이블2 = 주문_테이블_등록되어_있음(비어있는_주문_테이블2).as(OrderTableResponse.class);

        TableGroupRequest 단체지정요청 = TableGroupRequest.from(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()));
        TableGroupResponse 단체지정 = 단체_지정_등록되어_있음(단체지정요청).as(TableGroupResponse.class);

        ExtractableResponse<Response> response = 단체_지정_취소_요청함(단체지정.getId());

        단체_지정_취소됨(response);
    }

    /**
     * Given 메뉴 그룹 등록되어 있음
     * And 상품 등록되어 있음
     * And 메뉴 등록되어 있음
     * And 주문 테이블 여러개 등록되어 있음
     * And 단체 지정 되어 있음
     * And 주문(조리) 등록되어 있음
     * When 단체 지정 취소 요청함
     * Then 단체 지정 취소 실패함
     */
    @DisplayName("단체 지정된 주문 테이블들의 상태가 조리이면 단체 지정을 취소할 수 없다.")
    @Test
    void ungroupFail() {
        // Given 메뉴 그룹 등록되어 있음
        MenuGroupResponse 두마리메뉴 = MenuGroupRestAssured.메뉴_그룹_등록되어_있음(MenuGroupRequest.from("두마리메뉴")).as(MenuGroupResponse.class);

        // And 상품 등록되어 있음
        ProductResponse 후라이드 = ProductRestAssured.상품_등록되어_있음(ProductRequest.of("후라이드", BigDecimal.valueOf(16_000)))
                .as(ProductResponse.class);

        // And 메뉴 등록되어 있음
        List<MenuProductRequest> 메뉴상품목록 = Arrays.asList(MenuProductRequest.of(후라이드.getId(), 2));
        MenuResponse 후라이드치킨 = MenuRestAssured.메뉴_등록되어_있음(
                MenuRequest.of("후라이드치킨", BigDecimal.valueOf(16_000), 두마리메뉴.getId(), 메뉴상품목록)
        ).as(MenuResponse.class);

        // And 주문 테이블 여러개 등록되어 있음
        OrderTableResponse 등록된_주문_테이블1 = 주문_테이블_등록되어_있음(비어있는_주문_테이블1).as(OrderTableResponse.class);
        OrderTableResponse 등록된_주문_테이블2 = 주문_테이블_등록되어_있음(비어있는_주문_테이블2).as(OrderTableResponse.class);

        // And 단체 지정 되어 있음
        TableGroupRequest 단체지정요청 = TableGroupRequest.from(Arrays.asList(등록된_주문_테이블1.getId(), 등록된_주문_테이블2.getId()));
        TableGroupResponse 단체지정 = 단체_지정_등록되어_있음(단체지정요청).as(TableGroupResponse.class);

        // And 주문(조리) 등록되어 있음
        List<OrderLineItemRequest> 주문항목 = Arrays.asList(OrderLineItemRequest.of(후라이드치킨.getId(), 2));
        OrderRestAssured.주문_등록되어_있음(OrderRequest.of(등록된_주문_테이블1.getId(), 주문항목));
        OrderRestAssured.주문_등록되어_있음(OrderRequest.of(등록된_주문_테이블2.getId(), 주문항목));

        // When 단체 지정 취소 요청함
        ExtractableResponse<Response> response = 단체_지정_취소_요청함(단체지정.getId());

        // Then 단체 지정 취소 실패함
        단체_지정_취소_실패함(response);
    }

    /**
     * Given 메뉴 그룹 등록되어 있음
     * And 상품 등록되어 있음
     * And 메뉴 등록되어 있음
     * And 주문 테이블 여러개 등록되어 있음
     * And 단체 지정 되어 있음
     * And 주문(조리) 등록되어 있음
     * And 주문 상태(식사) 변경되어 있음
     * When 단체 지정 취소 요청함
     * Then 단체 지정 취소 실패함
     */
    @DisplayName("단체 지정된 주문 테이블들의 상태가 식사이면 단체 지정을 취소할 수 없다.")
    @Test
    void ungroupFail2() {
        // Given 메뉴 그룹 등록되어 있음
        MenuGroupResponse 두마리메뉴 = MenuGroupRestAssured.메뉴_그룹_등록되어_있음(MenuGroupRequest.from("두마리메뉴")).as(MenuGroupResponse.class);

        // And 상품 등록되어 있음
        ProductResponse 후라이드 = ProductRestAssured.상품_등록되어_있음(ProductRequest.of("후라이드", BigDecimal.valueOf(16_000)))
                .as(ProductResponse.class);

        // And 메뉴 등록되어 있음
        List<MenuProductRequest> 메뉴상품목록 = Arrays.asList(MenuProductRequest.of(후라이드.getId(), 2));
        MenuResponse 후라이드치킨 = MenuRestAssured.메뉴_등록되어_있음(
                MenuRequest.of("후라이드치킨", BigDecimal.valueOf(16_000), 두마리메뉴.getId(), 메뉴상품목록)
        ).as(MenuResponse.class);

        // And 주문 테이블 여러개 등록되어 있음
        OrderTableResponse 등록된_주문_테이블1 = 주문_테이블_등록되어_있음(비어있는_주문_테이블1).as(OrderTableResponse.class);
        OrderTableResponse 등록된_주문_테이블2 = 주문_테이블_등록되어_있음(비어있는_주문_테이블2).as(OrderTableResponse.class);

        // And 단체 지정 되어 있음
        TableGroupRequest 단체지정요청 = TableGroupRequest.from(Arrays.asList(등록된_주문_테이블1.getId(), 등록된_주문_테이블2.getId()));
        TableGroupResponse 단체지정 = 단체_지정_등록되어_있음(단체지정요청).as(TableGroupResponse.class);

        // And 주문(조리) 등록되어 있음
        List<OrderLineItemRequest> 주문항목 = Arrays.asList(OrderLineItemRequest.of(후라이드치킨.getId(), 2));
        OrderResponse 주문 = OrderRestAssured.주문_등록되어_있음(OrderRequest.of(등록된_주문_테이블1.getId(), 주문항목)).as(OrderResponse.class);
        OrderRestAssured.주문_등록되어_있음(OrderRequest.of(등록된_주문_테이블2.getId(), 주문항목));

        // And 주문 상태(식사) 변경되어 있음
        OrderRestAssured.주문_상태_변경_요청(주문.getId(), OrderRequest.from(OrderStatus.MEAL));

        // When 단체 지정 취소 요청함
        ExtractableResponse<Response> response = 단체_지정_취소_요청함(단체지정.getId());

        // Then 단체 지정 취소 실패함
        단체_지정_취소_실패함(response);
    }

    private void 단체_지정이됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 단체_지정_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 단체_지정_취소됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 단체_지정_취소_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
