package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.order.domain.OrderFixture.주문;
import static kitchenpos.table.domain.OrderTableFixture.주문테이블;
import static kitchenpos.table.domain.TableGroupFixture.테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 그룹 테스트")
public class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 테이블1;
    private OrderTable 테이블2;
    private OrderTable 테이블3;

    private TableGroup 테이블그룹;

    @BeforeEach
    void setup() {
        테이블1 = 주문테이블(1L, null, 0, true);
        테이블2 = 주문테이블(2L, null, 0, true);
        테이블3 = 주문테이블(3L, null, 0, true);
    }

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void 테이블_그룹_생성() {
        // given
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(테이블1));
        given(orderTableRepository.findById(2L)).willReturn(Optional.ofNullable(테이블2));
        given(orderTableRepository.findById(3L)).willReturn(Optional.ofNullable(테이블3));

        테이블그룹 = 테이블그룹(1L, Arrays.asList(테이블1, 테이블2, 테이블3));
        given(tableGroupRepository.save(any())).willReturn(테이블그룹);

        // when
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId(), 테이블3.getId()));
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        tableGroupResponse.getOrderTables().stream()
                .forEach(orderTable -> {
                    System.out.println(orderTable.getTableGroup().getId());
                    System.out.println(테이블그룹);
                    System.out.println(orderTable.isEmpty());
                });

        // then
        assertAll(
                () -> assertThat(tableGroupResponse.getOrderTables()).hasSize(3)
        );
    }

    @DisplayName("테이블 그룹을 해제한다")
    @Test
    void 테이블_그룹_해제() {
        // given
        테이블그룹 = 테이블그룹(1L);
        테이블그룹.group(Arrays.asList(테이블1, 테이블2, 테이블3));

        Order 주문1 = 주문(1L, OrderStatus.COMPLETION.name(), 테이블1);
        Order 주문2 = 주문(2L, OrderStatus.COMPLETION.name(), 테이블2);
        Order 주문3 = 주문(3L, OrderStatus.COMPLETION.name(), 테이블3);

        given(tableGroupRepository.findById(테이블그룹.getId())).willReturn(Optional.ofNullable(테이블그룹));
        given(orderRepository.findOrderByOrderTableId(주문1.getId())).willReturn(Optional.ofNullable(주문1));
        given(orderRepository.findOrderByOrderTableId(주문2.getId())).willReturn(Optional.ofNullable(주문2));
        given(orderRepository.findOrderByOrderTableId(주문3.getId())).willReturn(Optional.ofNullable(주문3));

        // when
        tableGroupService.ungroup(테이블그룹.getId());

        // then
        assertThat(테이블그룹.getOrderTables().stream()
                .allMatch(orderTable -> orderTable.getTableGroup() == null)).isTrue();

    }

    @DisplayName("단일 테이블로 테이블 그룹을 생성한다")
    @Test
    void 단일_테이블로_테이블_그룹_생성() {
        // when & then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(테이블1.getId()));

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroupRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 테이블로 테이블 그룹을 생성한다")
    @Test
    void 존재하지_않은_테이블로_테이블_그룹_생성() {
        // given
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(테이블1));
        given(orderTableRepository.findById(2L)).willReturn(Optional.ofNullable(null));

        // when & then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId()));

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroupRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 테이블로 테이블 그룹을 생성한다")
    @Test
    void 비어_있지_않은_테이블로_테이블_그룹_생성() {
        // given
        OrderTable 테이블4 = 주문테이블(4L, null, 5, false);

        // when & then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블4.getId()));

        assertThatThrownBy(
                () -> tableGroupService.create(tableGroupRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문상태가 완료가 아닌 주문이 있는 테이블이 포함된 테이블 그룹을 해제한다")
    @Test
    void 주문상태_완료가_아닌_주문이_있는_테이블의_테이블그룹_해제() {
        // given
        테이블그룹 = 테이블그룹(1L);
        테이블그룹.group(Arrays.asList(테이블1, 테이블2, 테이블3));

        Order 주문1 = 주문(1L, OrderStatus.COMPLETION.name(), 테이블1);
        Order 주문2 = 주문(2L, OrderStatus.COOKING.name(), 테이블2);

        given(tableGroupRepository.findById(테이블그룹.getId())).willReturn(Optional.ofNullable(테이블그룹));
        given(orderRepository.findOrderByOrderTableId(주문1.getId())).willReturn(Optional.ofNullable(주문1));
        given(orderRepository.findOrderByOrderTableId(주문2.getId())).willReturn(Optional.ofNullable(주문2));

        // when & then
        assertThatThrownBy(
                () -> tableGroupService.ungroup(테이블그룹.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }
}