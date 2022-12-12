package kitchenpos.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.BaseAcceptanceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class TableGroupAcceptanceTest extends BaseAcceptanceTest {

    MenuGroup 후라이드치킨_메뉴그룹 = new MenuGroup(1L, "후라이드치킨");
    ProductRequest 후라이드치킨_상품 = new ProductRequest(1L, "후라이드치킨", new BigDecimal(16000.00));
    MenuProductRequest 후라이드치킨_메뉴상품 = new MenuProductRequest(1L, 1L, 1L, 1);
    MenuRequest 후라이드치킨 = new MenuRequest(1L, "후라이드치킨", new BigDecimal(16000.00), 1L,
            Collections.singletonList(후라이드치킨_메뉴상품));
    OrderTable 주문_테이블;
    OrderTable 주문_테이블2;

    @Test
    void 단체_지정을_등록할_수_있다() throws Exception {
        TableGroup tableGroup = 단체_지정_가능한_주문_테이블_등록();

        ResultActions resultActions = 단체_지정(tableGroup);

        단체_지정_성공(resultActions);
    }

    @Test
    void 두개_이상의_주문_테이블만_단체_지정이_가능하다() throws Exception {
        OrderTable 주문_테이블 = 주문_테이블이_등록되어_있다(1L);

        ResultActions resultActions = 단체_지정(주문_테이블);

        단체_지정_실패(resultActions);
    }

    @Test
    void 주문_테이블은_필수로_지정해야_한다() throws Exception {
        OrderTable 주문_테이블을_지정하지_않은_경우 = null;

        ResultActions resultActions = 단체_지정(주문_테이블을_지정하지_않은_경우);

        단체_지정_실패(resultActions);
    }

    @Test
    void 등록_된_주문_테이블만_단체_지정이_가능하다() throws Exception {
        OrderTable 등록_되지_않은_주문_테이블 = new OrderTable(null, null, 2, true);
        OrderTable 등록_되지_않은_주문_테이블2 = new OrderTable(null, null, 2, true);

        ResultActions resultActions = 단체_지정(등록_되지_않은_주문_테이블, 등록_되지_않은_주문_테이블2);

        단체_지정_실패(resultActions);
    }

    @Test
    void 빈_테이블이_아닌_주문_테이블은_단체_지정이_불가능하다() throws Exception {
        OrderTable orderTable = 빈_테이블이_아닌_주문_테이블(1L);
        OrderTable orderTable2 = 빈_테이블이_아닌_주문_테이블(2L);

        ResultActions resultActions = 단체_지정(orderTable, orderTable2);

        단체_지정_실패(resultActions);
    }

    @Test
    void 이미_단체_지정이_된_주문_테이블은_단체_지정이_불가능하다() throws Exception {
        OrderTable orderTable = 이미_단체_지정이_된_주문_테이블();

        ResultActions resultActions = 단체_지정(orderTable);

        단체_지정_실패(resultActions);
    }

    @Test
    void 단체_지정을_해제할_수_있다() throws Exception {
        TableGroup tableGroup = 단체_지정이_되어있다();

        ResultActions resultActions = 단체_지정_해제(tableGroup);

        단체_지정_해제_성공(resultActions);
    }

    @Test
    void 주문_테이블에_조리_식사_상태가_포함된_주문이_있을경우_해제가_불가능하다() throws Exception {
        TableGroup tableGroup = 단체_지정_해제가_불가능한_단체();

        ResultActions resultActions = 단체_지정_해제(tableGroup);

        단체_지정_해제_실패(resultActions);
    }

    private void 단체_지정_해제_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is4xxClientError());
    }

    private void 단체_지정_해제_성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isNoContent());
    }

    private ResultActions 단체_지정_해제(TableGroup tableGroup) throws Exception {
        return mvc.perform(delete("/api/table-groups/{tableGroupId}", tableGroup.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private TableGroup 단체_지정이_되어있다() throws Exception {
        TableGroup tableGroup = 단체_지정_가능한_주문_테이블_등록();
        ResultActions resultActions = 단체_지정(tableGroup);
        단체_지정_성공(resultActions);
        return tableGroup;
    }

    private TableGroup 단체_지정_해제가_불가능한_단체() throws Exception {
        TableGroup tableGroup = 단체_지정_가능한_주문_테이블_등록();
        ResultActions resultActions = 단체_지정(tableGroup);
        단체_지정_성공(resultActions);
        주문_테이블_비어있음_여부_수정(주문_테이블);
        주문_테이블_비어있음_여부_수정(주문_테이블2);
        주문이_등록되어_있다(주문_테이블);
        주문이_등록되어_있다(주문_테이블2);
        return tableGroup;
    }

    private ResultActions 주문_테이블_비어있음_여부_수정(OrderTable orderTable) throws Exception {
        return mvc.perform(put("/api/tables/{orderTableId}/empty", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private Order 주문이_등록되어_있다(OrderTable orderTable) throws Exception {
        메뉴그룹_등록(후라이드치킨_메뉴그룹);
        상품_등록(후라이드치킨_상품);
        메뉴_등록(후라이드치킨);
        Order 주문 = new Order(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(new OrderLineItem(1L, 1L, 1L, 1)));
        주문_등록(주문);
        return 주문;
    }

    private ResultActions 메뉴_등록(MenuRequest menu) throws Exception {
        return mvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 주문_등록(Order order) throws Exception {
        return mvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 메뉴그룹_등록(MenuGroup menuGroup) throws Exception {
        return mvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions 상품_등록(ProductRequest product) throws Exception {
        return mvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private OrderTable 빈_테이블이_아닌_주문_테이블(Long id) throws Exception {
        OrderTable 주문_테이블 = new OrderTable(null, null, 1, false);
        주문_테이블_등록(주문_테이블);
        주문_테이블.setId(id);
        return 주문_테이블;
    }

    private OrderTable 이미_단체_지정이_된_주문_테이블() throws Exception {
        주문_테이블 = new OrderTable(null, null, 1, true);
        주문_테이블2 = new OrderTable(null, null, 1, true);
        주문_테이블_등록(주문_테이블);
        주문_테이블_등록(주문_테이블2);
        주문_테이블.setId(1L);
        주문_테이블2.setId(2L);
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));
        mvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
        return 주문_테이블;
    }

    private void 단체_지정_성공(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().isCreated());
    }

    private TableGroup 단체_지정_가능한_주문_테이블_등록() throws Exception {
        주문_테이블 = new OrderTable(null, null, 1, true);
        주문_테이블2 = new OrderTable(null, null, 1, true);
        주문_테이블_등록(주문_테이블);
        주문_테이블_등록(주문_테이블2);
        주문_테이블.setId(1L);
        주문_테이블2.setId(2L);
        return new TableGroup(1L, LocalDateTime.now(), Arrays.asList(주문_테이블, 주문_테이블2));
    }

    private ResultActions 단체_지정(OrderTable... orderTable) throws Exception {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(orderTable));
        return 단체_지정(tableGroup);
    }

    private ResultActions 단체_지정(TableGroup tableGroup) throws Exception {
        return mvc.perform(post("/api/table-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tableGroup))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private OrderTable 주문_테이블이_등록되어_있다(Long id) throws Exception {
        OrderTable 주문_테이블 = new OrderTable(null, null, 1, false);
        주문_테이블_등록(주문_테이블);
        주문_테이블.setId(id);
        return 주문_테이블;
    }

    private ResultActions 주문_테이블_등록(OrderTable orderTable) throws Exception {
        return mvc.perform(post("/api/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderTable))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private void 단체_지정_실패(ResultActions resultActions) throws Exception {
        resultActions.andExpect(status().is4xxClientError());
    }
}
