package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.product.domain.Product;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private Product 치킨;
    private MenuProduct 치킨상품;
    private MenuGroup 치킨단품;
    private Menu 후라이드치킨;
    private OrderLineItemRequest 후라이드치킨주문요청;
    private Order 주문;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private OrderTable 주문테이블3;
    private OrderTable 주문테이블4;

    @BeforeEach
    void setUp() {
        치킨 = Product.of(1L, "치킨", BigDecimal.valueOf(15000));
        치킨상품 = MenuProduct.of(치킨, 1);
        치킨단품 = MenuGroup.of(1L, "치킨단품");
        후라이드치킨 = Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(15000), 치킨단품.getId(), Collections.singletonList(치킨상품));
        후라이드치킨주문요청 = new OrderLineItemRequest(후라이드치킨.getId(), 2);
        주문테이블1 = OrderTable.of(1L, null, 5, true);
        주문테이블2 = OrderTable.of(2L, null, 4, false);
        주문테이블3 = OrderTable.of(3L, null, 5, true);
        주문테이블4 = OrderTable.of(4L, null, 4, true);
        주문 = Order.of(주문테이블2.getId(), OrderLineItems.of(Collections.singletonList(후라이드치킨주문요청.toOrderLineItem(OrderMenu.of(후라이드치킨)))));
    }

    @DisplayName("단체 지정을 한다.")
    @Test
    void 단체_지정() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(주문테이블3.getId(), 주문테이블4.getId()));
        TableGroup 단체 = TableGroup.of(1L);
        when(orderTableRepository.findById(주문테이블3.getId())).thenReturn(Optional.of(주문테이블3));
        when(orderTableRepository.findById(주문테이블4.getId())).thenReturn(Optional.of(주문테이블4));
        when(tableGroupRepository.save(any())).thenReturn(단체);

        // when
        TableGroupResponse saveTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertAll(
                () -> assertThat(주문테이블3.getTableGroupId()).isEqualTo(saveTableGroup.getId()),
                () -> assertThat(주문테이블4.getTableGroupId()).isEqualTo(saveTableGroup.getId()),
                () -> assertThat(주문테이블3.isEmpty()).isFalse(),
                () -> assertThat(주문테이블4.isEmpty()).isFalse()
        );
    }

    @DisplayName("테이블을 2개 이상 지정하지 않으면 단체 지정을 할 수 없다.")
    @Test
    void 단일_테이블_단체_지정() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문테이블1));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(TableGroup.of(1L));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Collections.singletonList(주문테이블1.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void 등록되지_않은_테이블_단체_지정() {
        // given
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("빈 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void 빈_테이블이_아닌_테이블_단체_지정() {
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderTableRepository.findById(주문테이블2.getId())).thenReturn(Optional.of(주문테이블2));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(TableGroup.of(1L));

        assertThatThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정된 테이블은 단체 지정을 할 수 없다.")
    @Test
    void 이미_단체_지정된_테이블_단체_지정() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(주문테이블2.getId(), 주문테이블3.getId()));
        when(orderTableRepository.findById(주문테이블2.getId())).thenReturn(Optional.of(주문테이블2));
        when(orderTableRepository.findById(주문테이블3.getId())).thenReturn(Optional.of(주문테이블3));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(TableGroup.of(1L));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void 단체_지정_해제() {
        // given
        TableGroup 단체 = TableGroup.of(1L);
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        when(tableGroupRepository.findById(단체.getId())).thenReturn(Optional.of(단체));
        when(orderTableRepository.findAllByTableGroupId(단체.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderRepository.findAllByOrderTableIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(Collections.singletonList(주문));

        // when
        tableGroupService.ungroup(단체.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroupId()).isNull(),
                () -> assertThat(주문테이블2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("조리중이거나 식사중인 테이블은 단체 지정을 해제할 수 없다.")
    @Test
    void 조리중_식사중인_테이블_단체_지정_해제() {
        // given
        TableGroup 단체 = TableGroup.of(1L);
        when(tableGroupRepository.findById(단체.getId())).thenReturn(Optional.of(단체));
        when(orderTableRepository.findAllByTableGroupId(단체.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderRepository.findAllByOrderTableIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(Collections.singletonList(주문));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(단체.getId()));
    }
}
