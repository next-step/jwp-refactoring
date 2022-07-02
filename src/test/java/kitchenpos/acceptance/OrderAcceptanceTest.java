package kitchenpos.acceptance;

import static kitchenpos.__fixture__.MenuProductTestFixture.메뉴_상품_1개_생성;
import static kitchenpos.__fixture__.OrderLineItemTestFixture.주문_항목_생성;
import static kitchenpos.acceptance.MenuAcceptanceTest.메뉴_생성_요청;
import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static kitchenpos.acceptance.TableAcceptanceTest.테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends AcceptanceTest {

    private Long 두마리_메뉴_아이디;
    private Long 후라이드_아이디;
    private Long 양념_아이디;
    private OrderTable 주문_테이블_비어있지_않음;
    private OrderTable 주문_테이블_비어있음;
    private Menu 메뉴_1;

    @BeforeEach
    public void setUp() {
        super.setUp();
        두마리_메뉴_아이디 = 메뉴_그룹_생성_요청("두마리메뉴").jsonPath().getLong("id");
        후라이드_아이디 = 상품_생성_요청("후라이드", 16_000).jsonPath().getLong("id");
        양념_아이디 = 상품_생성_요청("양념", 17_000).jsonPath().getLong("id");
        주문_테이블_비어있지_않음 = 테이블_생성_요청(3, false).as(OrderTable.class);
        주문_테이블_비어있음 = 테이블_생성_요청(5, true).as(OrderTable.class);
        메뉴_1 = 메뉴_생성_요청(
                "후라이드양념",
                31_000,
                두마리_메뉴_아이디,
                메뉴_상품_1개_생성(후라이드_아이디),
                메뉴_상품_1개_생성(양념_아이디)
        ).as(Menu.class);
    }

    @DisplayName("주문 생성에 성공한다.")
    @Test
    void createOrder() {
        //given
        final Long 메뉴_아이디 = 메뉴_1.getId();
        final Long 메뉴_수량 = 메뉴_1.getMenuProducts()
                .stream()
                .mapToLong(menuProduct -> menuProduct.getQuantity())
                .sum();
        final OrderLineItem 주문_항목 = 주문_항목_생성(메뉴_아이디, 메뉴_수량);

        //when
        final ExtractableResponse<Response> 결과 = 주문_생성_요청(주문_테이블_비어있지_않음.getId(), "COOKING", 주문_항목);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("주문 생성 시 주문 라인 아이템이 없으면 실패한다.")
    @Test
    void createOrderFailedWhenOrderLineItemNotExists() {
        //when
        final ExtractableResponse<Response> 결과 = 주문_생성_요청(주문_테이블_비어있지_않음.getId(), "COOKING");

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("주문 생성 시 저장되지 않은 메뉴를 주문하면 실패한다.")
    @Test
    void createOrderFailedWhenMenuNotExists() {
        //given
        final Long 존재하지_않는_메뉴_아이디 = 100L;
        final OrderLineItem 주문_항목 = 주문_항목_생성(존재하지_않는_메뉴_아이디, 2L);

        //when
        final ExtractableResponse<Response> 결과 = 주문_생성_요청(주문_테이블_비어있지_않음.getId(), "COOKING", 주문_항목);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("주문 생성 시 주문 테이블이 비어 있으면 실패한다.")
    @Test
    void createOrderFailedWhenOrderTableIsEmpty() {
        //given
        final Long 메뉴_아이디 = 메뉴_1.getId();
        final Long 메뉴_수량 = 메뉴_1.getMenuProducts()
                .stream()
                .mapToLong(menuProduct -> menuProduct.getQuantity())
                .sum();
        final OrderLineItem 주문_항목 = 주문_항목_생성(메뉴_아이디, 메뉴_수량);

        //when
        final ExtractableResponse<Response> 결과 = 주문_생성_요청(주문_테이블_비어있음.getId(), "COOKING", 주문_항목);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("주문 목록 조회 성공한다.")
    @Test
    void findOrders() {
        //given
        final Long 메뉴_아이디 = 메뉴_1.getId();
        final Long 메뉴_수량 = 메뉴_1.getMenuProducts()
                .stream()
                .mapToLong(menuProduct -> menuProduct.getQuantity())
                .sum();
        final OrderLineItem 주문_항목 = 주문_항목_생성(메뉴_아이디, 메뉴_수량);
        주문_생성_요청(주문_테이블_비어있지_않음.getId(), "COOKING", 주문_항목);

        //when
        final ExtractableResponse<Response> 결과 = 주문_목록_조회_요청();

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(결과.jsonPath().getList(".").size()).isEqualTo(1);
    }

    @DisplayName("주문 변경한다.")
    @Test
    void updateOrder() {
        //given
        final Long 메뉴_아이디 = 메뉴_1.getId();
        final Long 메뉴_수량 = 메뉴_1.getMenuProducts()
                .stream()
                .mapToLong(menuProduct -> menuProduct.getQuantity())
                .sum();
        final OrderLineItem 주문_항목 = 주문_항목_생성(메뉴_아이디, 메뉴_수량);
        final Order 생성된_주문 = 주문_생성_요청(주문_테이블_비어있지_않음.getId(), "COOKING", 주문_항목).as(Order.class);

        //when
        final ExtractableResponse<Response> 결과 = 주문_변경_요청(생성된_주문.getId(), 주문_테이블_비어있지_않음.getId(), "MEAL",
                주문_항목);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(결과.jsonPath().getString("orderStatus")).isEqualTo("MEAL");
    }

    @DisplayName("주문 변경 시 COMPLETION 주문 상태인 주문은 변경할 수 없다.")
    @Test
    void updateOrderFailedWhenOrderStatusIsCompletion() {
        //given
        final Long 메뉴_아이디 = 메뉴_1.getId();
        final Long 메뉴_수량 = 메뉴_1.getMenuProducts()
                .stream()
                .mapToLong(menuProduct -> menuProduct.getQuantity())
                .sum();
        final OrderLineItem 주문_항목 = 주문_항목_생성(메뉴_아이디, 메뉴_수량);
        final Order 생성된_주문 = 주문_생성_요청(주문_테이블_비어있지_않음.getId(), "COOKING", 주문_항목).as(Order.class);
        주문_변경_요청(생성된_주문.getId(), 주문_테이블_비어있지_않음.getId(), "COMPLETION", 주문_항목);

        //when
        final ExtractableResponse<Response> 결과 = 주문_변경_요청(생성된_주문.getId(), 주문_테이블_비어있지_않음.getId(), "MEAL",
                주문_항목);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 주문_생성_요청(final Long orderTableId, final String orderStatus,
                                                         final OrderLineItem orderLineItem) {
        final Order order = new Order(orderTableId, orderStatus, LocalDateTime.now(), orderLineItem);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(final Long orderTableId, final String orderStatus) {
        final Order order = new Order(orderTableId, orderStatus, LocalDateTime.now());

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when().post("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/orders")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 주문_변경_요청(final Long orderId, final Long orderTableId,
                                                         final String orderStatus,
                                                         final OrderLineItem orderLineItem) {
        final Order order = new Order(orderTableId, orderStatus, LocalDateTime.now(), orderLineItem);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("orderId", orderId)
                .body(order)
                .when().put("/api/orders/{orderId}/order-status")
                .then().log().all()
                .extract();
    }
}
