package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.exception.TableEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    private OrderService orderService;


    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;

    private List<OrderLineItem> orderLineItems;

    private List<OrderLineItemCreate> orderLineItemCreates;

    @BeforeEach
    void setUp() {
        this.orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

        orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1);
        orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 2);

        this.orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);

        this.orderLineItemCreates = this.orderLineItems.stream()
                .map(item -> new OrderLineItemCreate(item.getOldMenuId(), item.getQuantity()))
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("create - 등록을 원하는 주문항목이 DB에 전부 존재하는지 확인하여 전부 존재하지 않으면 IllegalArgumentException이 발생한다.")
    void 등록을_원하는_주문항목이_DB에_전부_존재하는지_확인하여_전부_존재하지_않으면_IllegalArgumentException이_발생한다() {
        // given
        OrderCreate orderCreate = new OrderCreate(1L, OrderStatus.MEAL, orderLineItemCreates);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(new OrderTable(1L, null, null,null, null, false)));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderCreate));
    }

    @Test
    @DisplayName("create - 주문 테이블이 존재하는지 확인하고, 없으면 EntityNotExistsException이 발생한다.")
    void 주문_테이블이_존재하는지_확인하고_없으면_EntityNotExistsException이_발생한다() {
        // given
        Long orderTableId = 1L;

        OrderCreate orderCreate = new OrderCreate(1L, OrderStatus.MEAL, orderLineItemCreates);

        // when
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(EntityNotExistsException.class)
                .isThrownBy(() -> orderService.create(orderCreate));

        verify(orderTableDao, VerificationModeFactory.times(1)).findById(orderTableId);
    }

    @Test
    @DisplayName("create - 주문 테이블이 빈 테이블일 경우 TableEmptyException이 발생한다.")
    void 주문_테이블이_빈_테이블일_경우_TableEmptyException이_발생한다() {
        // given
        Long orderTableId = 1L;

        OrderTable orderTable = new OrderTable(orderTableId, 1L, 1, true);

        OrderCreate orderCreate = new OrderCreate(1L, OrderStatus.MEAL, orderLineItemCreates);

        // when
        when(orderTableDao.findById(orderTableId)).thenReturn(Optional.of(orderTable));

        // then
        assertThatExceptionOfType(TableEmptyException.class)
                .isThrownBy(() -> orderService.create(orderCreate));

        verify(orderTableDao, VerificationModeFactory.times(1)).findById(orderTableId);

    }

    @Test
    @DisplayName("create - 정상적인 주문 테이블 등록")
    void 정상적인_주문_테이블_등록() {
        // given
        Long orderTableId = 1L;
        Long orderId = 1L;

        OrderTable orderTable = new OrderTable(orderTableId, 1L, 1, false);


        List<Menu> menus = Arrays.asList(
                new Menu(1L, "A", new Price(1), null, null),
                new Menu(2L, "A", new Price(2), null, null)
        );

        OrderCreate orderCreate = new OrderCreate(orderTableId, OrderStatus.MEAL, orderLineItemCreates);

        given(menuDao.findAllById(any())).willReturn(menus);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(orderTable));

        // when
        when(orderDao.save(any())).thenAnswer(i -> i.getArgument(0));

        Order savedOrder = orderService.create(orderCreate);

        // then

        assertThat(savedOrder.getOrderTable()).isEqualTo(orderTable);
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(savedOrder.getOrderedTime()).isNotNull();

        assertThat(savedOrder.getOrderLineItems())
                .map(item -> item.getOrder())
                .containsOnly(savedOrder);
        assertThat(savedOrder.getOrderLineItems())
                .map(item -> item.getMenu())
                .containsExactlyElementsOf(menus);

        verify(menuDao, VerificationModeFactory.times(1)).findAllById(any());
        verify(orderTableDao, VerificationModeFactory.times(1)).findById(orderTableId);
        verify(orderDao, VerificationModeFactory.times(1)).save(any());
    }

    @Test
    @DisplayName("create - 정상적인 주문 테이블 전체 조회")
    void 정상적인_주문_테이블_전체_조회() {
        // given
        Long orderTableId = 1L;
        Long orderId = 1L;

        Order order = new Order(orderId, orderTableId, OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems);

        // when
        when(orderDao.findAll()).thenReturn(Arrays.asList(order));

        Order savedOrder = orderService.list().get(0);

        // then
        assertThat(savedOrder).isEqualTo(order);
        assertThat(savedOrder.getOrderLineItems()).containsExactly(orderLineItem1, orderLineItem2);

        verify(orderDao, VerificationModeFactory.times(1)).findAll();
    }

    @Test
    @DisplayName("changeOrderStatus - 변경을 원하는 주문을 DB에서 가져오고, 없으면 IllegalArgumentException이 발생한다.")
    void 변경을_원하는_주문을_DB에서_가져오고_없으면_IllegalArgumentException이_발생한다(){
        // given
        Long orderId = 1L;
        // when
        when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(orderId, OrderStatus.MEAL));

        verify(orderDao, VerificationModeFactory.times(1)).findById(orderId);
    }

    @Test
    @DisplayName("changeOrderStatus - 주문의 상태가 계산 완료이고, 변경하려는 상태도 계산완료일 경우 IllegalArgumentException 이 발생한다.")
    void 주문의_상태가_계산_완료이고_변경하려는_상태도_계산완료일_경우_IllegalArgumentException이_발생한다() {
        // given
        Long orderId = 1L;

        Order order = new Order(orderId, 1L,
                null, OrderStatus.COMPLETION, LocalDateTime.now(),
                orderLineItems);

        // when
        when(orderDao.findById(orderId)).thenReturn(Optional.of(order));

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.changeOrderStatus(orderId, OrderStatus.COMPLETION));

        verify(orderDao, VerificationModeFactory.times(1)).findById(orderId);
    }

    @Test
    @DisplayName("changeOrderStatus - 정상적인 주문 상태 변경")
    void 정상적인_주문_상태_변경() {
        // given
        Long orderId = 1L;

        List<OrderLineItem> orderLineItems = Arrays.asList(
                new OrderLineItem(1L, 1L, 1L, 1L),
                new OrderLineItem(2L, 2L, 2L, 2L)
        );

        Order order = new Order(orderId, 1L,
                OrderStatus.COOKING.name(), LocalDateTime.now(),
                orderLineItems);

        given(orderDao.findById(orderId)).willReturn(Optional.of(order));

        // when

        Order savedOrder = orderService.changeOrderStatus(orderId, OrderStatus.COOKING);

        // then
        assertThat(savedOrder.getOldOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(savedOrder.getOrderLineItems()).containsExactlyElementsOf(orderLineItems);

        verify(orderDao, VerificationModeFactory.times(1)).findById(orderId);
    }
}