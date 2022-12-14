package kitchenpos.tablegroup.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.EntityNotFoundExceptionCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderExceptionCode;
import kitchenpos.tablegroup.domain.OrderTable;
import kitchenpos.tablegroup.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.OrderTableExceptionCode;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("주문과 주문 테이블 간의 validation 클래스 테스트")
@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
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
    private TableValidator tableValidator;

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

    @Test
    void 주문_생성시_등록되지_않은_주문_테이블이면_주문을_생성할_수_없음() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableValidator.validateToCreateOrder(주문테이블.getId(), Arrays.asList(MENU_ID1, MENU_ID2));
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(EntityNotFoundExceptionCode.NOT_FOUND_BY_ID.getMessage());
    }

    @Test
    void 주문_생성시_등록되지_않은_메뉴가_포함되어_있으면_주문을_생성할_수_없음() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        given(menuRepository.findAllById(Arrays.asList(MENU_ID1, MENU_ID2))).willReturn(Arrays.asList(양식_세트1));

        assertThatThrownBy(() -> {
            tableValidator.validateToCreateOrder(주문테이블.getId(), Arrays.asList(MENU_ID1, MENU_ID2));
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(EntityNotFoundExceptionCode.NOT_FOUND_BY_ID.getMessage());
    }

    @Test
    void 주문_생성시_주문_테이블이_비어있으면_주문을_생성할_수_없음() {
        OrderTable 빈_주문테이블 = new OrderTable(0, true);
        given(orderTableRepository.findById(EMPTY_ORDER_TABLE_ID)).willReturn(Optional.of(빈_주문테이블));
        given(menuRepository.findAllById(Arrays.asList(MENU_ID1, MENU_ID2))).willReturn(Arrays.asList(양식_세트1, 양식_세트2));

        assertThatThrownBy(() -> {
            tableValidator.validateToCreateOrder(EMPTY_ORDER_TABLE_ID, Arrays.asList(MENU_ID1, MENU_ID2));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderExceptionCode.ORDER_TABLE_CANNOT_BE_EMPTY.getMessage());
    }

    @Test
    void 주문_생성시_1개의_메뉴도_포함되지_않았으면_주문을_생성할_수_없음() {
        given(orderTableRepository.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

        assertThatThrownBy(() -> {
            tableValidator.validateToCreateOrder(주문테이블.getId(), Collections.emptyList());
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderExceptionCode.MUST_BE_GREATER_THAN_MINIMUM_SIZE.getMessage());
    }

    @Test
    void 주문_테이블이_다른_테이블_그룹에_포함되어_있으면_빈_테이블로_변경할_수_없음() {
        OrderTable 주문테이블2 = new OrderTable(2, false);
        ReflectionTestUtils.setField(주문테이블2, "tableGroup", new TableGroup());

        assertThatThrownBy(() -> {
            tableValidator.validateToChangeEmpty(주문테이블2);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중이거나_식사중인_주문이_있으면_빈_테이블로_변경할_수_없음(OrderStatus orderStatus) {
        Order order = new Order(주문테이블.getId(), orderStatus);
        given(orderRepository.findAllByOrderTableId(주문테이블.getId())).willReturn(Arrays.asList(order));

        assertThatThrownBy(() -> {
            tableValidator.validateToChangeEmpty(주문테이블);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderExceptionCode.CANNOT_BE_CHANGED.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중이거나_식사중인_주문이_있으면_테이블_그룹을_해제할_수_없음(OrderStatus orderStatus) {
        Order order = new Order(주문테이블.getId(), orderStatus);
        given(orderRepository.findAllByOrderTableIds(Arrays.asList(주문테이블.getId()))).willReturn(Arrays.asList(order));

        assertThatThrownBy(() -> {
            tableValidator.validateToUngroup(Arrays.asList(주문테이블.getId()));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderExceptionCode.CANNOT_BE_CHANGED.getMessage());
    }
}
