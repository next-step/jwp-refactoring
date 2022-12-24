package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.tablegroup.application.OrderTableValidator;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static kitchenpos.exception.EntityNotFoundExceptionConstants.NOT_FOUND_BY_ID;
import static kitchenpos.order.exception.OrderExceptionConstants.CANNOT_BE_CHANGED_ORDER_STATUS;
import static kitchenpos.tablegroup.exception.TableExceptionConstants.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP;
import static kitchenpos.tablegroup.exception.TableExceptionConstants.MUST_BE_GREATER_THAN_MINIMUM_ORDER_MENU_SIZE;
import static kitchenpos.tablegroup.exception.TableExceptionConstants.ORDER_TABLES_CANNOT_BE_EMPTY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {
    private static final Long MENU_ID1 = 1L;
    private static final Long MENU_ID2 = 2L;
    private static final Long EMPTY_ORDER_TABLE_ID = 2L;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableValidator orderTableValidator;

    private MenuGroup 양식;
    private Menu 양식_세트1;
    private Menu 양식_세트2;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup("양식");
        양식_세트1 = new Menu("양식 세트1", new BigDecimal(50000), 양식);
        양식_세트2 = new Menu("양식 세트2", new BigDecimal(43000), 양식);
        주문테이블 = new OrderTable(2, false);

        ReflectionTestUtils.setField(주문테이블, "id", 1L);
    }

    @DisplayName("주문 생성 시 등록되지 않은 주문 테이블이면 주문을 생성할 수 없다.")
    @Test
    void orderValidation_isRegisterOrderTable() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderTableValidator.validateToCreateOrder(주문테이블.getId(), Arrays.asList(MENU_ID1, MENU_ID2));
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(NOT_FOUND_BY_ID.getErrorMessage());
    }

    @DisplayName("주문 생성 시 등록되지 않은 메뉴가 포함되어 있으면 주문을 생성할 수 없다.")
    @Test
    void orderValidation_isRegisterMenu() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

        assertThatThrownBy(() -> {
            orderTableValidator.validateToCreateOrder(주문테이블.getId(), Arrays.asList(MENU_ID1, MENU_ID2));
        }).isInstanceOf(kitchenpos.exception.EntityNotFoundException.class)
                .hasMessage(NOT_FOUND_BY_ID.getErrorMessage());
    }

    @DisplayName("주문 생성 시 테이블이 비어 있으면 주문을 생성할 수 없다.")
    @Test
    void orderValidation_emptyTable() {
        OrderTable 빈_주문테이블 = new OrderTable(0, true);
        given(orderTableRepository.findById(EMPTY_ORDER_TABLE_ID)).willReturn(Optional.of(빈_주문테이블));
        given(menuRepository.findAllById(Arrays.asList(MENU_ID1, MENU_ID2))).willReturn(Arrays.asList(양식_세트1, 양식_세트2));

        assertThatThrownBy(() -> {
            orderTableValidator.validateToCreateOrder(EMPTY_ORDER_TABLE_ID, Arrays.asList(MENU_ID1, MENU_ID2));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ORDER_TABLES_CANNOT_BE_EMPTY.getErrorMessage());
    }

    @DisplayName("주문 생성 시 메뉴가 1개도 없으면 주문을 생성할 수 없다.")
    @Test
    void orderValidation_hasNoMenu() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

        assertThatThrownBy(() -> {
            orderTableValidator.validateToCreateOrder(주문테이블.getId(), Collections.emptyList());
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MUST_BE_GREATER_THAN_MINIMUM_ORDER_MENU_SIZE.getErrorMessage());
    }

    @DisplayName("주문 테이블이 다른 테이블 그룹에 포함되어 있으면 빈 테이블로 변경할 수 없다.")
    @Test
    void orderTableValidation_containsOtherTableGroup() {
        OrderTable 주문테이블2 = new OrderTable(2, false);
        ReflectionTestUtils.setField(주문테이블2, "tableGroup", new TableGroup());

        assertThatThrownBy(() -> {
            orderTableValidator.validateToChangeEmpty(주문테이블2);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getErrorMessage());
    }

    @DisplayName("조리중이거나 식사중인 주문이 있으면 빈 테이블로 변경할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void modifyOrderTableValidation_tableStatus(OrderStatus orderStatus) {
        Order order = Order.from(주문테이블.getId());
        OrderLineItem orderLineItem1 = OrderLineItem.of(order, OrderMenu.of(양식_세트1), 1L);
        OrderLineItem orderLineItem2 = OrderLineItem.of(order, OrderMenu.of(양식_세트2), 1L);
        order.addOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        order.changeOrderStatus(orderStatus);
        given(orderRepository.findAllByOrderTableId(주문테이블.getId())).willReturn(Arrays.asList(order));

        assertThatThrownBy(() -> {
            orderTableValidator.validateToChangeEmpty(주문테이블);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
    }

    @DisplayName("조리중이거나 식사중인 주문이 있으면 테이블 그룹을 해제할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void deleteOrderTableValidation_tableStatus(OrderStatus orderStatus) {
        Order order = Order.from(주문테이블.getId());
        OrderLineItem orderLineItem1 = OrderLineItem.of(order, OrderMenu.of(양식_세트1), 1L);
        OrderLineItem orderLineItem2 = OrderLineItem.of(order, OrderMenu.of(양식_세트2), 1L);
        order.addOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        given(orderRepository.findAllByOrderTableIds(Arrays.asList(주문테이블.getId()))).willReturn(Arrays.asList(order));

        assertThatThrownBy(() -> {
            orderTableValidator.validateToUngroup(Arrays.asList(주문테이블.getId()));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CANNOT_BE_CHANGED_ORDER_STATUS.getErrorMessage());
    }
}
