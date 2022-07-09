package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuDuplicateException;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.exception.*;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderTable 저장된_주문테이블;
    private Menu 첫번째_메뉴;
    private Menu 두번째_메뉴;
    private OrderLineItem 첫번째_주문항목;
    private OrderLineItem 두번째_주문항목;

    private Order 생성할_주문;
    private Order 저장된_주문;

    @BeforeEach
    void setUp() {
        MenuGroup 메뉴그룹 = new MenuGroup(1L, "추천메뉴");
        첫번째_메뉴 = new Menu(1L, "짬짜면세트", BigDecimal.valueOf(0), 메뉴그룹, Collections.emptyList());
        두번째_메뉴 = new Menu(2L, "탕수육세트", BigDecimal.valueOf(0), 메뉴그룹, Collections.emptyList());

        저장된_주문테이블 = new OrderTable(1L, null, 4, false);
        첫번째_주문항목 = new OrderLineItem(첫번째_메뉴, 2);
        두번째_주문항목 = new OrderLineItem(두번째_메뉴, 1);

        List<OrderLineItem> 주문항목_목록 = Arrays.asList(첫번째_주문항목, 두번째_주문항목);
        생성할_주문 = new Order(저장된_주문테이블.getId(), 주문항목_목록);
        저장된_주문 = new Order(1L, 저장된_주문테이블.getId(), 주문항목_목록);
    }

    @DisplayName("주문을 생성할 수 있다.")
    @Test
    void createOrder() {
        // given
        OrderRequest 생성할_주문_요쳥 = new OrderRequest(1L,
                Arrays.asList(OrderLineItemDto.from(첫번째_주문항목), OrderLineItemDto.from(두번째_주문항목)));

        given(menuRepository.countByIdIn(anyList()))
                .willReturn((long) 생성할_주문_요쳥.getOrderLineItems().size());
        given(menuRepository.findById(eq(1L)))
                .willReturn(Optional.ofNullable(첫번째_메뉴));
        given(menuRepository.findById(eq(2L)))
                .willReturn(Optional.ofNullable(두번째_메뉴));
        given(orderTableRepository.findById(생성할_주문_요쳥.getOrderTableId()))
                .willReturn(Optional.of(저장된_주문테이블));
        given(orderRepository.save(생성할_주문))
                .willReturn(저장된_주문);

        // when
        OrderResponse 생성된_주문_응답 = orderService.create(생성할_주문_요쳥);

        // then
        주문_생성_성공(생성된_주문_응답, 생성할_주문_요쳥);
    }


    @DisplayName("주문 항목이 존재하지 않으면 주문 생성에 실패한다.")
    @Test
    void createOrderFailsWhenEmptyOrderLineItems() {
        // given
        OrderRequest 생성할_주문_요쳥 = new OrderRequest(1L, Arrays.asList(OrderLineItemDto.from(첫번째_주문항목)));

        given(menuRepository.countByIdIn(anyList()))
                .willReturn((long) 생성할_주문_요쳥.getOrderLineItems().size());
        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        주문_생성_실패_주문항목_없음(생성할_주문_요쳥);
    }

    @DisplayName("중복된 메뉴가 있으면 주문 생성에 실패한다.")
    @Test
    void createOrderFailsWhenDuplicateMenu() {
        // given
        OrderRequest 생성할_주문_요쳥 = new OrderRequest(1L,
                Arrays.asList(OrderLineItemDto.from(첫번째_주문항목), OrderLineItemDto.from(첫번째_주문항목)));

        // when & then
        주문_생성_실패_중목메뉴_존재(생성할_주문_요쳥);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 주문 생성에 실패한다.")
    @Test
    void createOrderFailsWhenNoOrderTable() {
        // given
        Long 존재하지_않는_주문테이블ID = 1000L;
        OrderRequest 생성할_주문_요쳥 = new OrderRequest(존재하지_않는_주문테이블ID, Arrays.asList(OrderLineItemDto.from(첫번째_주문항목)));

        given(menuRepository.countByIdIn(anyList()))
                .willReturn((long) 생성할_주문_요쳥.getOrderLineItems().size());
        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(첫번째_메뉴));

        // when & then
        주문_생성_실패_주문테이블_없음(생성할_주문_요쳥);
    }

    @DisplayName("주문 테이블이 빈 테이블이면 주문 생성에 실패한다.")
    @Test
    void createOrderFailsWhenEmptyOrderTable() {
        // given
        Long 빈_주문테이블ID = 2L;
        OrderTable 빈_주문테이블 = new OrderTable(빈_주문테이블ID, null, 4, true);
        OrderRequest 생성할_주문_요쳥 = new OrderRequest(빈_주문테이블ID, Arrays.asList(OrderLineItemDto.from(첫번째_주문항목)));

        given(menuRepository.countByIdIn(anyList()))
                .willReturn((long) 생성할_주문_요쳥.getOrderLineItems().size());
        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.ofNullable(첫번째_메뉴));
        given(orderTableRepository.findById(빈_주문테이블ID))
                .willReturn(Optional.ofNullable(빈_주문테이블));

        // when & then
        주문_생성_실패_주문테이블_비어있음(생성할_주문_요쳥);
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void listOrders() {
        // given
        List<Order> 조회할_주문_목록 = Arrays.asList(저장된_주문);

        given(orderRepository.findAll())
                .willReturn(조회할_주문_목록);

        // when
        List<OrderResponse>  조회된_주문_목록_응답 = orderService.list();

        // then
        주문_목록_조회_성공(조회된_주문_목록_응답, 조회할_주문_목록);
    }


    @DisplayName("주문 테이블 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        Long 상태_변경할_주문ID = 1L;
        OrderStatus 변경후_상태 = OrderStatus.COMPLETION;
        Order 상태_변경된_주문 = new Order(1L, 저장된_주문테이블.getId(), OrderStatus.COMPLETION, Arrays.asList(첫번째_주문항목));

        given(orderRepository.findById(any()))
                .willReturn(Optional.of(저장된_주문));

        // when
        OrderResponse 상태_변경_주문_응답 = orderService.changeOrderStatus(상태_변경할_주문ID, new OrderRequest(변경후_상태.name()));

        // then
        주문_상태_변경_성공(상태_변경_주문_응답, 상태_변경된_주문);
    }

    @DisplayName("주문이 존재하지 않으면 상태 변경에 실패한다.")
    @Test
    void changeOrderStatusFailsWhenNoOrder() {
        Long 존재하지_않는_주문ID = 1000L;
        String 계산완료 = OrderStatus.COMPLETION.name();

        // when & then
        주문_상태_변경_실패_주문_없음(존재하지_않는_주문ID, new OrderRequest(계산완료));
    }

     @DisplayName("'계산 완료' 상태인 경우 상태 변경에 실패한다.")
     @Test
     void changeOrderStatusFailsWhenCompleted() {
         // given
         Order 계산완료_주문 = new Order(1L, 저장된_주문테이블.getId(), OrderStatus.COMPLETION, Arrays.asList(첫번째_주문항목));
         String 계산완료 = OrderStatus.COMPLETION.name();

         given(orderRepository.findById(계산완료_주문.getId()))
                 .willReturn(Optional.of(계산완료_주문));

         // when & then
         주문_상태_변경_실패_계산완료(계산완료_주문.getId(), new OrderRequest(계산완료));
     }


    private void 주문_생성_성공(OrderResponse 생성된_주문, OrderRequest 생성할_주문) {
        assertAll(
                () -> assertThat(생성된_주문.getId())
                        .isNotNull(),
                () -> assertThat(생성된_주문.getOrderedTime())
                        .isNotNull(),
                () -> assertThat(생성된_주문.getOrderStatus())
                        .isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(생성된_주문.getOrderTableId())
                        .isEqualTo(생성할_주문.getOrderTableId()),
                () -> assertThat(생성된_주문.getOrderLineItems())
                        .hasSize(생성할_주문.getOrderLineItems().size())
        );
    }

    private void 주문_생성_실패_주문항목_없음(OrderRequest 생성할_주문_요청) {
        assertThatThrownBy(() -> orderService.create(생성할_주문_요청))
                .isExactlyInstanceOf(OrderLineItemNotFoundException.class)
                .hasMessage("주문 항목 정보가 존재하지 않습니다.");
    }

    private void 주문_생성_실패_중목메뉴_존재(OrderRequest 생성할_주문_요청) {
        assertThatThrownBy(() -> orderService.create(생성할_주문_요청))
                .isExactlyInstanceOf(MenuDuplicateException.class)
                .hasMessage("주문 시 주문 항목에 메뉴들은 중복될 수 없습니다.");
    }

    private void 주문_생성_실패_주문테이블_없음(OrderRequest 생성할_주문_요청) {
        assertThatThrownBy(() -> orderService.create(생성할_주문_요청))
                .isExactlyInstanceOf(OrderTableNotFoundException.class)
                .hasMessage("주문 테이블 정보가 존재하지 않습니다.");
    }

    private void 주문_생성_실패_주문테이블_비어있음(OrderRequest 생성할_주문_요청) {
        assertThatThrownBy(() -> orderService.create(생성할_주문_요청))
                .isExactlyInstanceOf(InvalidOrderTableException.class)
                .hasMessage("주문 시 주문 테이블은 비어있을 수 없습니다.");
    }

    private void 주문_목록_조회_성공(List<OrderResponse> 조회된_주문_목록_응답, List<Order> 조회할_주문_목록) {
        assertAll(
                () -> assertThat(조회된_주문_목록_응답).hasSize(조회할_주문_목록.size()),
                () -> assertThat(조회된_주문_목록_응답.get(0).getId())
                        .isEqualTo(조회할_주문_목록.get(0).getId())
        );

    }

    private void 주문_상태_변경_성공(OrderResponse 상태_변경_주문_응답, Order 상태_변경된_주문) {
        assertThat(상태_변경_주문_응답.getOrderStatus())
                .isEqualTo(상태_변경된_주문.getOrderStatus().name());
    }

    private void 주문_상태_변경_실패_주문_없음(Long 주문ID, OrderRequest 변경할_주문_요청) {
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문ID, 변경할_주문_요청))
                .isExactlyInstanceOf(OrderNotFoundException.class)
                .hasMessage("주문 정보가 존재하지 않습니다.");
    }

    private void 주문_상태_변경_실패_계산완료(Long 주문ID, OrderRequest 변경할_주문_요청) {
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문ID, 변경할_주문_요청))
                .isExactlyInstanceOf(OrderAlreadyCompletedException.class)
                .hasMessage("계산 완료된 주문은 상태를 변경할 수 없습니다.");
    }
}
