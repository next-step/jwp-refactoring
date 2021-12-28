package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.utils.RequestUtil;
import kitchenpos.utils.StatusCodeCheckUtil;

@DisplayName("식당 관리 기능")
public class KitchenPosAcceptanceTest extends AcceptanceTest {
    @Test
    void 식당을_관리() {
        // when
        final ExtractableResponse<Response> productCreateResponse = 상품_생성_요청("후라이드치킨", 16000);

        // then
        상품_생성됨(productCreateResponse);

        // when
        final ExtractableResponse<Response> productFindResponse = 상품_조회_요청();

        // then
        상품_조회됨(productFindResponse);

        // when
        final ExtractableResponse<Response> menuGroupCreateResponse = 메뉴_그룹_생성_요청("순살메뉴");

        // then
        메뉴_그룹_생성됨(menuGroupCreateResponse);

        // when
        final ExtractableResponse<Response> menuGroupFindResponse = 메뉴_그룹_조회_요청();

        // then
        메뉴_그룹_조회됨(menuGroupFindResponse);

        // given
        final Long 메뉴_그룹_아이디 = extractId(menuGroupCreateResponse);
        final Long 상품아이디 = extractId(productCreateResponse);

        // when
        final ExtractableResponse<Response> menuCreateResponse =
            메뉴_생성_요청("후라이드치킨", 16000, 메뉴_그룹_아이디, 상품아이디, 1);

        // then
        메뉴_생성됨(menuCreateResponse);

        // when
        final ExtractableResponse<Response> menuFindResponse = 메뉴_조회_요청();

        // then
        메뉴_조회됨(menuFindResponse);

        // when
        final ExtractableResponse<Response> tableCreateResponse = 테이블_생성_요청(1, false);

        // then
        테이블_생성됨(tableCreateResponse);

        // given
        final Long 테이블아이디 = extractId(tableCreateResponse);
        final Long 메뉴아이디 = extractId(menuCreateResponse);

        // when
        final ExtractableResponse<Response> orderCreateResponse = 주문_생성_요청(테이블아이디, 메뉴아이디, 1);

        // then
        주문_생성됨(orderCreateResponse);

        // when
        final ExtractableResponse<Response> orderFindResponse = 주문_조회_요청();

        // then
        주문_조회됨(orderFindResponse);

        // given
        final Long 주문아이디 = extractId(orderCreateResponse);
        final Map<String, Object> 주문 = extractObject(orderCreateResponse);
        주문.put("orderStatus", "MEAL");

        // when
        final ExtractableResponse<Response> orderStatusChangeResponse = 주문_상태_변경(주문아이디, 주문);

        // then
        주문_상태_변경됨(orderStatusChangeResponse);

        // when
        final ExtractableResponse<Response> tableFindResponse = 테이블_조회_요청();

        // then
        테이블_조회됨(tableFindResponse);

        // given
        final ExtractableResponse<Response> newTableCreateResponse = 테이블_생성_요청(2, true);
        final Long 새로운_테이블아이디 = extractId(newTableCreateResponse);
        final Map<String, Object> 상태_변경할_테이블 = extractObject(newTableCreateResponse);
        상태_변경할_테이블.put("empty", false);

        // when
        final ExtractableResponse<Response> tableEmptyChangeResponse = 테이블_상태_변경_요청(새로운_테이블아이디, 상태_변경할_테이블);

        // then
        테이블_상태_변경됨(tableEmptyChangeResponse);

        // when
        final Map<String, Object> 방문한_손님_수_변경할_테이블 = extractObject(newTableCreateResponse);
        방문한_손님_수_변경할_테이블.put("numberOfGuests", 10);
        final ExtractableResponse<Response> tableGuestChangeResponse =
            테이블_방문한_손님_수_변경_요청(새로운_테이블아이디, 방문한_손님_수_변경할_테이블);

        // then
        테이블_방문한_손님_수_변경됨(tableGuestChangeResponse);

        // given
        final Map<String, Object> 단체_지정_테이블1 = extractObject(테이블_생성_요청(5, true));
        final Map<String, Object> 단체_지정_테이블2 = extractObject(테이블_생성_요청(7, true));

        // when
        final ExtractableResponse<Response> tableGroupingResponse = 테이블_단체_지정_요청(단체_지정_테이블1, 단체_지정_테이블2);

        // then
        테이블_단체_지정됨(tableGroupingResponse);

        // given
        final Long 테이블_그룹아이디 = extractId(tableGroupingResponse);

        // when
        final ExtractableResponse<Response> tableUngroupingResponse = 테이블_단체_지정_해제_요청(테이블_그룹아이디);

        // then
        테이블_단체_지정_해제됨(tableUngroupingResponse);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(final String name, final int price) {
        final Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("price", price);
        return RequestUtil.post("/api/products", product);
    }

    public static void 상품_생성됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.created(response);
        assertThat(response.header("Location")).isNotNull();
        assertThat(extractId(response)).isNotNull();
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return RequestUtil.get("/api/products");
    }

    public static void 상품_조회됨(final ExtractableResponse<Response> response) {
        목록_조회_됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(final String name) {
        final Map<String, Object> menuGroup = new HashMap<>();
        menuGroup.put("name", name);
        return RequestUtil.post("/api/menu-groups", menuGroup);
    }

    public static void 메뉴_그룹_생성됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.created(response);
        assertThat(response.header("Location")).isNotNull();
        assertThat(extractId(response)).isNotNull();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RequestUtil.get("/api/menu-groups");
    }

    public static void 메뉴_그룹_조회됨(final ExtractableResponse<Response> response) {
        목록_조회_됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(
        final String name, final int price, final Long menuGroupId, final Long productId, final int quantity
    ) {
        final Map<String, Object> menu = new HashMap<>();
        menu.put("name", name);
        menu.put("price", price);
        menu.put("menuGroupId", menuGroupId);

        final Map<String, Object> menuProduct = new HashMap<>();
        menuProduct.put("productId", productId);
        menuProduct.put("quantity", quantity);

        menu.put("menuProducts", Collections.singletonList(menuProduct));

        return RequestUtil.post("/api/menus", menu);
    }

    public static void 메뉴_생성됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.created(response);
        assertThat(response.header("Location")).isNotNull();
        assertThat(extractId(response)).isNotNull();
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RequestUtil.get("/api/menus");
    }

    public static void 메뉴_조회됨(final ExtractableResponse<Response> response) {
        목록_조회_됨(response);
    }

    public static ExtractableResponse<Response> 테이블_생성_요청(final int numberOfGuests, final boolean empty) {
        final Map<String, Object> orderTable = new HashMap<>();
        orderTable.put("numberOfGuests", numberOfGuests);
        orderTable.put("empty", empty);

        return RequestUtil.post("/api/tables", orderTable);
    }

    public static void 테이블_생성됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.created(response);
        assertThat(response.header("Location")).isNotNull();
        assertThat(extractId(response)).isNotNull();
    }

    public static ExtractableResponse<Response> 주문_생성_요청(
        final Long orderTableId, final Long menuId, final int quantity
    ) {
        final Map<String, Object> order = new HashMap<>();
        order.put("orderTableId", orderTableId);

        final Map<String, Object> orderLineItem = new HashMap<>();
        orderLineItem.put("menuId", menuId);
        orderLineItem.put("quantity", quantity);

        order.put("orderLineItems", Collections.singletonList(orderLineItem));

        return RequestUtil.post("/api/orders", order);
    }

    public static void 주문_생성됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.created(response);
        assertThat(response.header("Location")).isNotNull();
        assertThat(extractId(response)).isNotNull();
    }

    public static ExtractableResponse<Response> 주문_조회_요청() {
        return RequestUtil.get("/api/orders");
    }

    public static void 주문_조회됨(final ExtractableResponse<Response> response) {
        목록_조회_됨(response);
    }

    public static ExtractableResponse<Response> 주문_상태_변경(final Long orderId, final Map<String, Object> order) {
        return RequestUtil.put("/api/orders/{orderId}/order-status", order, orderId);
    }

    public static void 주문_상태_변경됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);
    }

    public static ExtractableResponse<Response> 테이블_조회_요청() {
        return RequestUtil.get("/api/tables");
    }

    public static void 테이블_조회됨(final ExtractableResponse<Response> response) {
        목록_조회_됨(response);
    }

    public static ExtractableResponse<Response> 테이블_상태_변경_요청(
        final Long orderTableId, final Map<String, Object> orderTable
    ) {
        return RequestUtil.put("/api/tables/{orderTableId}/empty", orderTable, orderTableId);
    }

    public static void 테이블_상태_변경됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);
    }

    public static ExtractableResponse<Response> 테이블_방문한_손님_수_변경_요청(
        final Long orderTableId, final Map<String, Object> orderTable
    ) {
        return RequestUtil.put("/api/tables/{orderTableId}/number-of-guests", orderTable, orderTableId);
    }

    public static void 테이블_방문한_손님_수_변경됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);
    }

    public static ExtractableResponse<Response> 테이블_단체_지정_요청(
        final Map<String, Object> orderTable1, final Map<String, Object> orderTable2
    ) {
        final Map<String, Object> tableGroup = new HashMap<>();
        tableGroup.put("orderTables", Arrays.asList(orderTable1, orderTable2));

        return RequestUtil.post("/api/table-groups", tableGroup);
    }

    public static void 테이블_단체_지정됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.created(response);
        assertThat(response.header("Location")).isNotNull();
        assertThat(extractId(response)).isNotNull();
    }

    public static ExtractableResponse<Response> 테이블_단체_지정_해제_요청(final Long tableGroupId) {
        return RequestUtil.delete("/api/table-groups/{tableGroupId}", tableGroupId);
    }

    public static void 테이블_단체_지정_해제됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.noContent(response);
    }

    private static Long extractId(final ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("id", Long.class);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> extractObject(final ExtractableResponse<Response> response) {
        return response.jsonPath().getObject(".", Map.class);
    }

    private static void 목록_조회_됨(final ExtractableResponse<Response> response) {
        StatusCodeCheckUtil.ok(response);
        final List<Object> objects = response.jsonPath().getList(".");
        assertThat(objects).isNotEmpty();
    }
}
