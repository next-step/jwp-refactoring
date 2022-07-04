package kitchenpos.order.application;

import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuFixture.메뉴그룹_생성;
import static kitchenpos.fixture.MenuFixture.메뉴상품_생성;
import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderFixture.주문요청_생성;
import static kitchenpos.fixture.OrderFixture.주문항목_생성;
import static kitchenpos.fixture.TableFixture.테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderValidator orderValidator;
    @InjectMocks
    private OrderService orderService;

    private Product product;
    private MenuGroup menuGroup;
    private Menu menu;
    private OrderTable orderTable;
    private List<OrderLineItem> orderLineItems;
    private Order order;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        product = ProductFixture.상품_생성(1L, "봉골레파스타", 13000);
        menuGroup = 메뉴그룹_생성(1L, "파스타메뉴");
        menu = 메뉴_생성("봉골레파스타세트", product.getPrice(), 1L, Arrays.asList(메뉴상품_생성(1L, 1L, 1L)));
        orderTable = 테이블_생성(1L, 2, false);
        orderLineItems = Arrays.asList(주문항목_생성(0L, menu.getId(), 1L));
        order = 주문_생성(1L, orderTable.getId(), OrderStatus.COOKING, orderLineItems);
        orderRequest = 주문요청_생성(orderTable.getId(), orderLineItems);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        //given
        given(orderRepository.save(any())).willReturn(order);

        //when
        OrderResponse orderResponse = orderService.create(orderRequest);

        //then
        assertThat(orderResponse).isNotNull();
        assertThat(orderResponse.getOrderTableId()).isEqualTo(orderRequest.getOrderTableId());
    }

    @DisplayName("주문 항목이 없으면, 주문 등록에 실패한다.")
    @Test
    void create_invalidOrderLineItemsSize() {
        //given
        orderRequest = 주문요청_생성(orderTable.getId(), new ArrayList<>());
        willThrow(new IllegalArgumentException("주문 항목이 1개 이상이어야 합니다."))
                .given(orderValidator)
                .validate(orderRequest);

        //when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문 항목이 1개 이상이어야 합니다.");
    }

    @DisplayName("주문 항목이 존재하지 않는 메뉴라면, 주문 등록에 실패한다.")
    @Test
    void create_invalidNotExistsMenu() {
        //given
        willThrow(new IllegalArgumentException("존재하지 않는 메뉴입니다."))
                .given(orderValidator)
                .validate(orderRequest);

        //when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 메뉴입니다.");
    }

    @DisplayName("주문의 테이블이 존재하지 않는 테이블이라면, 주문 등록에 실패한다.")
    @Test
    void create_invalidNotExistsTable() {
        //given
        willThrow(new IllegalArgumentException("주문테이블이 존재하지 않습니다."))
                .given(orderValidator)
                .validate(orderRequest);

        //when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문테이블이 존재하지 않습니다.");
    }

    @DisplayName("주문의 테이블이 빈 테이블이라면, 주문 등록에 실패한다.")
    @Test
    void create_invalidEmptyTable() {
        //given
        willThrow(new IllegalArgumentException("빈 테이블은 등록할 수 없습니다."))
                .given(orderValidator)
                .validate(orderRequest);

        //when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 테이블은 등록할 수 없습니다.");
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        //given
        given(orderRepository.findAll()).willReturn(Arrays.asList(order));

        //when
        List<OrderResponse> orderResponses = orderService.list();

        //then
        assertThat(orderResponses).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        //given
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        OrderRequest changeOrderStatusRequest = new OrderRequest(null, null, OrderStatus.COOKING.name());

        //when
        OrderResponse changedOrder = orderService.changeOrderStatus(order.getId(), changeOrderStatusRequest);

        //then
        assertThat(changedOrder).isNotNull();
        assertThat(changedOrder.getOrderStatus()).isEqualTo(changeOrderStatusRequest.getOrderStatus());
    }

    @DisplayName("주문 상태가 완료인 주문의 상태는 변경할 수 없다.")
    @Test
    void changeOrderStatus_invalidNotExistsOrder() {
        //given
        order = 주문_생성(1L, orderTable.getId(), OrderStatus.COMPLETION, orderLineItems);
        given(orderRepository.findById(any())).willReturn(Optional.of(order));
        orderRequest = new OrderRequest(null, null, OrderStatus.COOKING.name());

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("완료된 주문의 상태는 수정할 수 없습니다.");
    }

    @DisplayName("존재하지 않는 주문의 상태는 변경할 수 없다.")
    @Test
    void changeOrderStatus_invalidCompletion() {
        //given
        given(orderRepository.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 주문입니다.");
    }

}
