package kitchenpos.acceptence;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.*;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.order.domain.type.OrderStatus;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.*;
import kitchenpos.common.domain.Price;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptence.MenuGroupRestControllerTest.메뉴그룹을_생성한다;
import static kitchenpos.acceptence.MenuRestControllerTest.메뉴를_생성한다;
import static kitchenpos.acceptence.ProductRestControllerTest.상품을_등록한다;
import static kitchenpos.acceptence.TableRestControllerTest.주문테이블을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRestControllerTest extends AcceptanceSupport {
    private ProductResponse 치킨;
    private ProductResponse 제로콜라;
    private MenuGroupResponse 양식_메뉴_그륩;
    private MenuGroup 양식;
    private MenuResponse 후치콜세트_메뉴_생성;
    private TableResponse 주문테이블;
    private OrderTable 주문테이블_일번;
    private OrderTable 주문테이블_이번;
    private TableGroup 테이블_그륩;
    private OrderLineItem 후치콜_세트_주문_아이템;
    private Order 주문;
    private Menu 치킨_콜라_정식_메뉴;
    private OrderLineItemRequest 후치콜세트_주문요청;

    @BeforeEach
    public void setUp() {
        super.setUp();
        Product productA = new Product(new Price(BigDecimal.valueOf(3_000)), "후라이드치킨");
        Product productB = new Product(new Price(BigDecimal.valueOf(2_000)), "제로콜라");
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(new MenuProduct(productA, 2L), new MenuProduct(productB, 2L)));

        치킨 = 상품을_등록한다(new ProductRequest("치킨", BigDecimal.valueOf(10_000))).as(ProductResponse.class);
        제로콜라 = 상품을_등록한다(new ProductRequest("제로콜라", BigDecimal.valueOf(1_000))).as(ProductResponse.class);
        양식_메뉴_그륩 = 메뉴그룹을_생성한다(new MenuGroupRequest("양식_메뉴_그륩")).as(MenuGroupResponse.class);
        양식 = new MenuGroup(양식_메뉴_그륩.getId(), 양식_메뉴_그륩.getName());

        치킨_콜라_정식_메뉴 = new Menu("불고기정식", new Price(BigDecimal.valueOf(12_000)), 양식.getId(), menuProducts);

        MenuProductRequest productRequestA = new MenuProductRequest(치킨.getId(), 1L);
        MenuProductRequest productRequestB = new MenuProductRequest(제로콜라.getId(), 2L);


        MenuRequest request = new MenuRequest(
                치킨_콜라_정식_메뉴.getName(), 치킨_콜라_정식_메뉴.getPrice().getPrice(), 치킨_콜라_정식_메뉴.getMenuGroupId(),
                Arrays.asList(productRequestA, productRequestB)
        );


        후치콜세트_메뉴_생성 = 메뉴를_생성한다(request).as(MenuResponse.class);
        후치콜세트_주문요청 = new OrderLineItemRequest(후치콜세트_메뉴_생성.getId(), 1L);

        주문테이블 = 주문테이블을_생성한다(new TableRequest(0, false)).as(TableResponse.class);

        주문테이블_일번 = new OrderTable(1L, null, 3, true);
        주문테이블_이번 = new OrderTable(2L, null, 7, true);
        테이블_그륩 = new TableGroup();
        후치콜_세트_주문_아이템 = new OrderLineItem(치킨_콜라_정식_메뉴, 1L);
        주문 = new Order(주문테이블_일번.getId(), OrderStatus.COOKING);
    }

    @Test
    @DisplayName("주문을 등록 할 수 있다.")
    void createOrder() {
        // when
        ExtractableResponse<Response> response = 주문_생성을_요청한다(주문테이블.getId(), Arrays.asList(후치콜세트_주문요청));

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("주문 상태를 변경 할 수 있다.")
    void updateOrderStatus() {
        // given
        OrderResponse 주문_응답 = 주문_생성을_요청한다(주문테이블.getId(), Arrays.asList(후치콜세트_주문요청)).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_상태_수정_요청(주문_응답.getId(), OrderStatus.COMPLETION);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        주문_상태가_변경되었는지_확인한다(response, OrderStatus.COMPLETION.name());
    }

    @Test
    @DisplayName("주문 리스트를 받을 수 있다.")
    void getOrderList() {
        주문_생성을_요청한다(주문테이블.getId(), Arrays.asList(후치콜세트_주문요청)).as(OrderResponse.class);

        // when
        ExtractableResponse<Response> response = 주문_리스트를_요청한다();

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        주문_리스트를_비교한다(response, Arrays.asList(주문테이블.getId()));
    }

    private ExtractableResponse<Response> 주문_생성을_요청한다(Long orderTableId, List<OrderLineItemRequest> request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new OrderRequest(orderTableId, request)).when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    private void 상태값을_비교한다(int statusCode, HttpStatus created) {
        assertThat(statusCode).isEqualTo(created.value());
    }

    private ExtractableResponse<Response> 주문_리스트를_요청한다() {
        return RestAssured
                .given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 주문_상태_수정_요청(long id, OrderStatus status) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ChangeOrderStatusRequest(status))
                .when().put("/api/orders/{id}/order-status", id)
                .then().log().all()
                .extract();
    }

    private void 주문_리스트를_비교한다(ExtractableResponse<Response> response, List<Long> getId) {
        List<OrderResponse> result = response.jsonPath().getList(".", OrderResponse.class);
        List<Long> responseId = result.stream().map(OrderResponse::getId).collect(Collectors.toList());
        assertThat(responseId).containsAll(getId);
    }

    private void 주문_상태가_변경되었는지_확인한다(ExtractableResponse<Response> response, String expect) {
        String result = response.jsonPath().getString("orderStatus");
        assertThat(result).isEqualTo(expect);
    }
}
