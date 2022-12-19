package kitchenpos.ordertable.acceptance;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.dto.OrderTableChangeEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableChangeNumberOfGuestsRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

class TableAcceptanceTest extends BaseAcceptanceTest {

    MenuGroupRequest 후라이드치킨_메뉴그룹 = new MenuGroupRequest("후라이드치킨");
    ProductRequest 후라이드치킨_상품 = new ProductRequest(1L, "후라이드치킨", new BigDecimal(16000.00));
    MenuProductRequest 후라이드치킨_메뉴상품 = new MenuProductRequest(1L, 1L, 1L, 1);
    MenuRequest 후라이드치킨 = new MenuRequest(1L, "후라이드치킨", new BigDecimal(16000.00), 1L,
            Collections.singletonList(후라이드치킨_메뉴상품));
    OrderTableChangeNumberOfGuestsRequest orderTableChangeNumberOfGuestsRequest =
            new OrderTableChangeNumberOfGuestsRequest(1);
    OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(true);

    @Test
    void 주문_테이블을_등록할_수_있다() throws Exception {
        OrderTableRequest 주문_테이블 = new OrderTableRequest(null, 1, false);

        ResultActions resultActions = 주문_테이블_등록(주문_테이블);

        주문_테이블_등록_성공(resultActions, 주문_테이블);
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있다() throws Exception {
        OrderTableRequest orderTable = 주문_테이블이_등록되어_있다();

        ResultActions resultActions = 주문_테이블_목록_조회();

        주문_테이블_목록_조회_성공(resultActions, orderTable);
    }

    @Test
    void 등록_된_주문_테이블에_대해서만_수정할_수_있다() throws Exception {
        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(orderTableChangeEmptyRequest);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 주문_테이블의_비어있음_여부를_수정할_수_있다() throws Exception {
        주문_테이블이_등록되어_있다();

        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(orderTableChangeEmptyRequest);

        주문_테이블_수정_성공(resultActions);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_수정할_수_없다() throws Exception {
        주문_테이블에_이미_단체_지정이_되어있다();

        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(orderTableChangeEmptyRequest);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 조리_식사_상태의_주문이_포함되어_있으면_수정할_수_없다() throws Exception {
        주문_테이블에_조리_식사_상태의_주문이_포함되어_있다();

        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(orderTableChangeEmptyRequest);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 방문한_손님수를_0명_이하로_수정할_수_없다() throws Exception {
        주문_테이블이_등록되어_있다();

        ResultActions resultActions = 주문_테이블_방문한_손님수_수정(new OrderTableChangeNumberOfGuestsRequest(-1));

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 등록_된_주문_테이블에_대해서만_손님수를_수정할_수_있다() throws Exception {
        ResultActions resultActions = 주문_테이블_방문한_손님수_수정(orderTableChangeNumberOfGuestsRequest);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 빈_테이블은_방문한_손님수를_수정할_수_없다() throws Exception {
        빈_테이블이_등록되어_있다();

        ResultActions resultActions = 주문_테이블_방문한_손님수_수정(orderTableChangeNumberOfGuestsRequest);

        주문_테이블_수정_실패(resultActions);
    }

    @Test
    void 주문_테이블의_방문한_손님수를_수정할_수_있다() throws Exception {
        주문_테이블이_등록되어_있다();

        ResultActions resultActions = 주문_테이블_방문한_손님수_수정(orderTableChangeNumberOfGuestsRequest);

        주문_테이블_방문한_손님수_수정_성공(resultActions);
    }

    private void 주문_테이블_방문한_손님수_수정_성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    private OrderTableRequest 빈_테이블이_등록되어_있다() throws Exception {
        OrderTableRequest orderTable = 주문_테이블이_등록되어_있다();

        ResultActions resultActions = 주문_테이블_비어있음_여부_수정(new OrderTableChangeEmptyRequest(true));
        주문_테이블_수정_성공(resultActions);
        return orderTable;
    }

    private OrderTableRequest 주문_테이블에_조리_식사_상태의_주문이_포함되어_있다() throws Exception {
        OrderTableRequest orderTable = 주문_테이블이_등록되어_있다();
        주문이_등록되어_있다(orderTable);
        return orderTable;
    }

    private OrderRequest 주문이_등록되어_있다(OrderTableRequest orderTable) throws Exception {
        메뉴그룹_등록(후라이드치킨_메뉴그룹);
        상품_등록(후라이드치킨_상품);
        메뉴_등록(후라이드치킨);
        주문_테이블_등록(orderTable);
        OrderRequest 주문 = new OrderRequest(1L,
                Collections.singletonList(new OrderLineItemRequest(1L, 1l)));
        주문_등록(주문);
        return 주문;
    }

    private ResultActions 메뉴_등록(MenuRequest menu) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions 주문_등록(OrderRequest order) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions 메뉴그룹_등록(MenuGroupRequest menuGroup) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions 상품_등록(ProductRequest product) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private void 주문_테이블_수정_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    private ResultActions 주문_테이블_방문한_손님수_수정(OrderTableChangeNumberOfGuestsRequest request) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.put("/api/tables/{orderTableId}/number-of-guests", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private ResultActions 주문_테이블_비어있음_여부_수정(OrderTableChangeEmptyRequest request) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.put("/api/tables/{orderTableId}/empty", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private void 주문_테이블_목록_조회_성공(ResultActions resultActions, OrderTableRequest orderTable) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].tableGroupId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].numberOfGuests").value(orderTable.getNumberOfGuests()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].empty").value(orderTable.isEmpty()));
    }

    private OrderTableRequest 주문_테이블이_등록되어_있다() throws Exception {
        OrderTableRequest 주문_테이블 = new OrderTableRequest(1L, 1, false);
        ResultActions resultActions = 주문_테이블_등록(주문_테이블);
        주문_테이블_등록_성공(resultActions, 주문_테이블);
        return 주문_테이블;
    }

    private ResultActions 주문_테이블_목록_조회() throws Exception {
        return mvc.perform(MockMvcRequestBuilders.get("/api/tables")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private void 주문_테이블_수정_성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void 주문_테이블_등록_성공(ResultActions resultActions, OrderTableRequest orderTable) throws Exception {
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("tableGroupId").isEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("numberOfGuests").value(orderTable.getNumberOfGuests()))
                .andExpect(MockMvcResultMatchers.jsonPath("empty").value(orderTable.isEmpty()));
    }

    private ResultActions 주문_테이블_등록(OrderTableRequest orderTable) throws Exception {
        return mvc.perform(MockMvcRequestBuilders.post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
    }

    private OrderTableRequest 주문_테이블에_이미_단체_지정이_되어있다() throws Exception {
        OrderTableRequest 주문_테이블 = new OrderTableRequest(null, 1, true);
        OrderTableRequest 주문_테이블2 = new OrderTableRequest(null, 1, true);
        주문_테이블_등록(주문_테이블);
        주문_테이블_등록(주문_테이블2);
        주문_테이블.setId(1L);
        주문_테이블2.setId(2L);
        TableGroupRequest tableGroup = new TableGroupRequest(Arrays.asList(주문_테이블, 주문_테이블2));
        mvc.perform(MockMvcRequestBuilders.post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());
        return 주문_테이블;
    }
}
