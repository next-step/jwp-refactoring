package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.application.ProductService;
import kitchenpos.application.TableService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

public class OrderAcceptanceTest extends MockMvcAcceptanceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    TableService tableService;

    @Autowired
    ProductService productService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    MenuService menuService;

    /**
     * Feature: 주문하기
     *  Background:
     *      given: 테이블이 생성되어있음
     *      And: 여러 상품이 기존에 생성되어 있음
     *      And: 메뉴 그룹이 기존에 생성되어 있음
     *      And: 메뉴가 기존에 생성되어 있음
     *
     *  Scenario: 주문하기
     *      given: 메뉴를 선택해서
     *      when: 주문을 시도하면
     *      then: 주문이 성공되고, 주문 정보가 조회된다.
     */
    @Test
    @DisplayName("주문 성공")
    void orderSuccessTest() throws Exception {
        // given
        OrderTable 테이블 = tableService.create(new OrderTable(2, false));
        Product 상품1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product 상품2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup 메뉴그룹1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu 메뉴1 = menuService.create(new Menu("메뉴1", new BigDecimal(1000), 메뉴그룹1.getId(), Arrays.asList(
                new MenuProduct(상품1.getId(), 1),
                new MenuProduct(상품2.getId(), 1)
        )));

        // when
        ResultActions 주문_요청_결과 = 주문_요청(테이블, 메뉴1);

        // then
        주문_요청_결과
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andDo(print());
    }

    /**
     * Background:
     *  given: 테이블이 미리 생성되어있음
     *
     * Scenario: 메뉴 없이 주문하면 주문에 실패한다.
     *  given: 매뉴정보 없이
     *  when: 주문을 시도하면
     *  then: 메뉴 정보가 없어서 주문에 실패한다.
     */
    @Test
    @DisplayName("주문한 메뉴가 없으면 주문에 실패한다.")
    void orderFailTest1() throws Exception {
        // given
        OrderTable 테이블 = tableService.create(new OrderTable(2, false));

        // when & then
        assertThatThrownBy(
                () -> 주문_메뉴_없이_요청(테이블)
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * Background:
     *  given: 테이블이 미리 생성되어있음
     *
     * Scenario: 없는 메뉴를 주문하면 주문에 실패한다.
     *  given: 존재하지 않는 메뉴 정보로
     *  when: 주문을 시도하면
     *  then: 메뉴 정보가 없어서 주문에 실패한다.
     */
    @Test
    @DisplayName("없는 메뉴를 주문하면 주문에 실패한다.")
    void orderFailTest2() throws Exception {
        // given
        OrderTable 테이블 = tableService.create(new OrderTable(2, false));
        Menu 등록되지_않은_메뉴 = new Menu("등록되지 않은 메뉴", new BigDecimal(12000), 1L, Arrays.asList(
                new MenuProduct(1L, 1), new MenuProduct(2L, 1)
        ));

        // when & then
        assertThatThrownBy(
                () -> 주문_요청(테이블, 등록되지_않은_메뉴)
        ).hasCause(new IllegalArgumentException());
    }

    /**
     * Background:
     *  given: 테이블이 미리 생성되어있음
     *
     * Scenario: 비어있는 테이블에서 주문하면 주문에 실패한다.
     *  given: 비어있는 테이블에서
     *  when: 주문을 시도하면
     *  then: 메뉴 정보가 없어서 주문에 실패한다.
     */
    @Test
    @DisplayName("비어있는 테이블에서 주문 시도 시, 주문에 실패한다.")
    void orderFailTest3(){
        // given
        OrderTable 비어있는_테이블 = tableService.create(new OrderTable(2, true));
        Product 상품1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product 상품2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup 메뉴그룹1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu 메뉴1 = menuService.create(new Menu("메뉴1", new BigDecimal(1000), 메뉴그룹1.getId(), Arrays.asList(
                new MenuProduct(상품1.getId(), 1),
                new MenuProduct(상품2.getId(), 1)
        )));

        // when & then
        assertThatThrownBy(
                () -> 주문_요청(비어있는_테이블, 메뉴1)
        ).hasCause(new IllegalArgumentException());

        // then
    }

    /**
     * Feature: 주문하기
     *  Background:
     *      given: 테이블이 생성되어있음
     *      And: 여러 상품이 기존에 생성되어 있음
     *      And: 메뉴 그룹이 기존에 생성되어 있음
     *      And: 메뉴가 기존에 생성되어 있음
     *      And: 정상적인 주문이 요청되어 있음
     *
     *  Scenario: 주문의 상태를 변경한다.
     *      when: 조리 중인 주문의 상태를 식사 중으로 변경하면
     *      then: 주문의 상태가 식사중으로 변경되고,
     *      when: 식사 중인 주문의 상태를 계산완료로 변경하면
     *      then: 주문의 상태가 계산완료로 변경된다.
     */
    @Test
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatusTest() throws Exception {
        // given
        OrderTable 테이블 = tableService.create(new OrderTable(2, false));
        Product 상품1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product 상품2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup 메뉴그룹1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu 메뉴1 = menuService.create(new Menu("메뉴1", new BigDecimal(1000), 메뉴그룹1.getId(), Arrays.asList(
                new MenuProduct(상품1.getId(), 1),
                new MenuProduct(상품2.getId(), 1)
        )));
        ResultActions 주문_요청_결과 = 주문_요청(테이블, 메뉴1);
        Order 최초_주문 = getObjectByResponse(주문_요청_결과, Order.class);
        assertThat(최초_주문.getId()).isNotNull();
        주문_상태_변경됨(최초_주문, OrderStatus.COOKING);

        // when
        ResultActions 식사중_상태_변경_요청_결과 = 주문_상태_변경_요청(최초_주문, OrderStatus.MEAL);
        Order 식사중_주문 = getObjectByResponse(식사중_상태_변경_요청_결과, Order.class);

        // then
        주문_상태_변경됨(식사중_주문, OrderStatus.MEAL);

        // when
        ResultActions 계산완료_상태_변경_요청_결과 = 주문_상태_변경_요청(최초_주문, OrderStatus.COMPLETION);
        Order 계산완료_주문 = getObjectByResponse(계산완료_상태_변경_요청_결과, Order.class);

        // then
        주문_상태_변경됨(계산완료_주문, OrderStatus.COMPLETION);
    }

    /**
     * Feature: 주문하기
     *  Background:
     *      given: 테이블이 생성되어있음
     *      And: 여러 상품이 기존에 생성되어 있음
     *      And: 메뉴 그룹이 기존에 생성되어 있음
     *      And: 메뉴가 기존에 생성되어 있음
     *      And: 정상적인 주문이 요청되어 있음
     *      And: 주문 상태가 계산완료 처리되어 있음
     *
     *  Scenario: 계산이 완료된 주문은 상태를 변경 할 수 없다.
     *      when: 계산완료 상태인 주문의 상태를 변경하려 하면
     *      then: 상태가 변경되지 않는다.
     */
    @Test
    @DisplayName("계산완료인 주문은 상태를 변경할 수 없다.")
    void changeOrderStatusFailTest() throws Exception {
        // given
        OrderTable 테이블 = tableService.create(new OrderTable(2, false));
        Product 상품1 = productService.create(new Product("상품1", new BigDecimal(1000)));
        Product 상품2 = productService.create(new Product("상품2", new BigDecimal(2000)));
        MenuGroup 메뉴그룹1 = menuGroupService.create(new MenuGroup("그룹1"));
        Menu 메뉴1 = menuService.create(new Menu("메뉴1", new BigDecimal(1000), 메뉴그룹1.getId(), Arrays.asList(
                new MenuProduct(상품1.getId(), 1),
                new MenuProduct(상품2.getId(), 1)
        )));
        ResultActions 주문_요청_결과 = 주문_요청(테이블, 메뉴1);
        Order 최초_주문 = getObjectByResponse(주문_요청_결과, Order.class);
        assertThat(최초_주문.getId()).isNotNull();
        주문_상태_변경됨(최초_주문, OrderStatus.COOKING);

        ResultActions 계산완료_상태_변경_요청_결과 = 주문_상태_변경_요청(최초_주문, OrderStatus.COMPLETION);
        Order 계산완료_주문 = getObjectByResponse(계산완료_상태_변경_요청_결과, Order.class);
        주문_상태_변경됨(계산완료_주문, OrderStatus.COMPLETION);

        // when & then
        assertThatThrownBy(
                () -> 주문_상태_변경_요청(계산완료_주문, OrderStatus.MEAL)
        ).hasCause(new IllegalArgumentException());
    }

    private void 주문_상태_변경됨(Order 최초_주문, OrderStatus orderStatus) {
        assertThat(최초_주문.getOrderStatus()).isEqualTo(orderStatus.name());
    }

    private ResultActions 주문_상태_변경_요청(Order 최초_주문, OrderStatus orderStatus) throws Exception {
        return mockPut("/api/orders/{orderId}/order-status", 최초_주문.getId(), new Order(orderStatus.name()));
    }

    private ResultActions 주문_요청(OrderTable orderTable, Menu menu) throws Exception {
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()
                , Collections.singletonList(new OrderLineItem(null, menu.getId(), 1)));
        return mockPost("/api/orders", order);
    }

    private ResultActions 주문_메뉴_없이_요청(OrderTable orderTable) throws Exception {
        Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now()
                , Collections.emptyList());
        return mockPost("/api/orders", order);
    }
}
