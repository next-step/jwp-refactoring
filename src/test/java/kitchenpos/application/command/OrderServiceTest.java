package kitchenpos.application.command;

import kitchenpos.application.query.OrderQueryService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderCreate;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItemCreate;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.dto.response.OrderViewResponse;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.exception.TableEmptyException;
import kitchenpos.fixture.CleanUp;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.fixture.MenuFixture.양념치킨_콜라_1000원_1개;
import static kitchenpos.fixture.MenuFixture.후라이드치킨_콜라_2000원_1개;
import static kitchenpos.fixture.OrderFixture.결제완료_음식_2;
import static kitchenpos.fixture.OrderFixture.식사_음식_1;
import static kitchenpos.fixture.OrderLineItemFixture.주문_연결_안된_양념치킨_콜라_1000원_1개;
import static kitchenpos.fixture.OrderLineItemFixture.주문_연결_안된_후라이드치킨_콜라_2000원_1개;
import static kitchenpos.fixture.OrderTableFixture.미사용중인_테이블;
import static kitchenpos.fixture.OrderTableFixture.사용중인_1명_테이블;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    private OrderService orderService;
    private OrderQueryService orderQueryService;

    private List<Menu> menus;

    private List<OrderLineItem> orderLineItems;

    private List<OrderLineItemCreate> orderLineItemCreates;

    @BeforeEach
    void setUp() {
        CleanUp.cleanUp();

        orderService = new OrderService(menuRepository, orderRepository, orderTableRepository);
        orderQueryService = new OrderQueryService(orderRepository);

        menus = Arrays.asList(양념치킨_콜라_1000원_1개, 후라이드치킨_콜라_2000원_1개);

        orderLineItems = Arrays.asList(주문_연결_안된_양념치킨_콜라_1000원_1개, 주문_연결_안된_후라이드치킨_콜라_2000원_1개);

        orderLineItemCreates = this.orderLineItems.stream()
                .map(item -> new OrderLineItemCreate(item.getMenuId(), item.getQuantity()))
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("create - 등록을 원하는 주문항목이 DB에 전부 존재하는지 확인하여 전부 존재하지 않으면 IllegalArgumentException이 발생한다.")
    void 등록을_원하는_주문항목이_DB에_전부_존재하는지_확인하여_전부_존재하지_않으면_IllegalArgumentException이_발생한다() {
        // given
        OrderCreate orderCreate = new OrderCreate(미사용중인_테이블.getId(), OrderStatus.MEAL, orderLineItemCreates);

        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(미사용중인_테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> orderService.create(orderCreate));
    }

    @Test
    @DisplayName("create - 주문 테이블이 존재하는지 확인하고, 없으면 EntityNotExistsException이 발생한다.")
    void 주문_테이블이_존재하는지_확인하고_없으면_EntityNotExistsException이_발생한다() {
        // given
        OrderCreate orderCreate = new OrderCreate(미사용중인_테이블.getId(), OrderStatus.MEAL, orderLineItemCreates);

        // when
        when(orderTableRepository.findById(미사용중인_테이블.getId())).thenReturn(Optional.empty());

        // then
        assertThatExceptionOfType(EntityNotExistsException.class)
                .isThrownBy(() -> orderService.create(orderCreate));

        verify(orderTableRepository, VerificationModeFactory.times(1))
                .findById(미사용중인_테이블.getId());
    }

    @Test
    @DisplayName("create - 주문 테이블이 빈 테이블일 경우 TableEmptyException이 발생한다.")
    void 주문_테이블이_빈_테이블일_경우_TableEmptyException이_발생한다() {
        // given
        OrderCreate orderCreate = new OrderCreate(1L, OrderStatus.MEAL, orderLineItemCreates);

        // when
        when(orderTableRepository.findById(미사용중인_테이블.getId())).thenReturn(Optional.of(미사용중인_테이블));

        // then
        assertThatExceptionOfType(TableEmptyException.class)
                .isThrownBy(() -> orderService.create(orderCreate));

        verify(orderTableRepository, VerificationModeFactory.times(1))
                .findById(미사용중인_테이블.getId());
    }

    @Test
    @DisplayName("create - 정상적인 주문 테이블 등록")
    void 정상적인_주문_테이블_등록() {
        // given
        OrderCreate orderCreate = new OrderCreate(사용중인_1명_테이블.getId(), OrderStatus.MEAL, orderLineItemCreates);

        given(menuRepository.findAllById(any())).willReturn(menus);

        given(orderTableRepository.findById(사용중인_1명_테이블.getId())).willReturn(Optional.of(사용중인_1명_테이블));

        // when
        when(orderRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        orderService.create(orderCreate);

        // then
        verify(menuRepository, VerificationModeFactory.times(1)).findAllById(any());
        verify(orderTableRepository, VerificationModeFactory.times(1)).findById(사용중인_1명_테이블.getId());
        verify(orderRepository, VerificationModeFactory.times(1)).save(any());
    }

    @Test
    @DisplayName("create - 정상적인 주문 테이블 전체 조회")
    void 정상적인_주문_테이블_전체_조회() {
        // when
        when(orderRepository.findAll()).thenReturn(Arrays.asList(결제완료_음식_2));

        OrderViewResponse savedOrder = orderQueryService.list().get(0);

        // then
        assertThat(savedOrder).isEqualTo(OrderViewResponse.of(결제완료_음식_2));

        verify(orderRepository, VerificationModeFactory.times(1)).findAll();
    }

    @Test
    @DisplayName("changeOrderStatus - 변경을 원하는 주문을 DB에서 가져오고, 없으면 IllegalArgumentException이 발생한다.")
    void 변경을_원하는_주문을_DB에서_가져오고_없으면_IllegalArgumentException이_발생한다(){
        // given
        Long orderId = 1L;

        // when
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(orderId, OrderStatus.MEAL));

        verify(orderRepository, VerificationModeFactory.times(1)).findById(orderId);
    }

    @Test
    @DisplayName("changeOrderStatus - 주문의 상태가 계산 완료이고, 변경하려는 상태도 계산완료일 경우 IllegalArgumentException 이 발생한다.")
    void 주문의_상태가_계산_완료이고_변경하려는_상태도_계산완료일_경우_IllegalArgumentException이_발생한다() {
        // given
        Long orderId = 1L;

        // when
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(OrderFixture.결제완료_음식_1));

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(orderId, OrderStatus.COMPLETION));

        verify(orderRepository, VerificationModeFactory.times(1)).findById(orderId);
    }

    @Test
    @DisplayName("changeOrderStatus - 정상적인 주문 상태 변경")
    void 정상적인_주문_상태_변경() {
        // given
        given(orderRepository.findById(식사_음식_1.getId())).willReturn(Optional.of(식사_음식_1));

        // when
        orderService.changeOrderStatus(식사_음식_1.getId(), OrderStatus.MEAL);

        // then
        assertThat(식사_음식_1.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        assertThat(식사_음식_1.getOrderLineItems()).containsExactlyElementsOf(식사_음식_1.getOrderLineItems());

        verify(orderRepository, VerificationModeFactory.times(1)).findById(식사_음식_1.getId());
    }
}