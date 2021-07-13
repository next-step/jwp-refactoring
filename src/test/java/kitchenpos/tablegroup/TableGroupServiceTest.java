package kitchenpos.tablegroup;

import kitchenpos.ordertable.application.TableGroupService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.TableGroupRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;


    private TableGroup 테이블그룹;
    private OrderTable 첫번째_테이블;
    private OrderTable 두번째_테이블;

    @BeforeEach
    void setUp() {
        첫번째_테이블 = new OrderTable();
        첫번째_테이블.setId(1L);
        첫번째_테이블.setEmpty(true);
        두번째_테이블 = new OrderTable();
        두번째_테이블.setId(2L);
        두번째_테이블.setEmpty(true);

        테이블그룹 = new TableGroup();
        테이블그룹.setId(1L);
    }

    @DisplayName("단체 지정을 등록한다")
    @Test
    void 단체지정_등록() {
        //Given
        테이블그룹.setOrderTables(Arrays.asList(첫번째_테이블, 두번째_테이블));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(첫번째_테이블, 두번째_테이블));
        when(tableGroupRepository.save(테이블그룹)).thenReturn(테이블그룹);
        when(orderTableRepository.save(첫번째_테이블)).thenReturn(첫번째_테이블);
        when(orderTableRepository.save(두번째_테이블)).thenReturn(두번째_테이블);

        //When
        TableGroup 생성된_단체지정 = tableGroupService.create(테이블그룹);

        //Then
        assertThat(생성된_단체지정.getOrderTables())
                .hasSize(2)
                .containsExactly(첫번째_테이블, 두번째_테이블);
    }

    @DisplayName("주문 테이블이 입력되지 않은 경우, 예외가 발생한다")
    @Test
    void 주문테이블_입력되지_않은_경우_예외발생() {
        //Given
        테이블그룹.setOrderTables(null);

        //When + Then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 1개만 입력된 경우, 예외가 발생한다")
    @Test
    void 주문테이블_1개만_입력된_경우_예외발생() {
        //Given
        테이블그룹.setOrderTables(Arrays.asList(첫번째_테이블));

        //When + Then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("중복된 주문 테이블이 입력된 경우, 예외가 발생한다")
    @Test
    void 주문테이블이_중복입력된_경우_예외발생() {
        //Given
        테이블그룹.setOrderTables(Arrays.asList(첫번째_테이블, 첫번째_테이블));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 1L))).thenReturn(Arrays.asList(첫번째_테이블));

        //When + Then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 단체 지정된 주문 테이블을 입력한 경우, 예외가 발생한다")
    @Test
    void 주문테이블이_이미_단체지정된_경우_예외발생() {
        //Given
        첫번째_테이블.setTableGroup(99L);
        테이블그룹.setOrderTables(Arrays.asList(첫번째_테이블, 두번째_테이블));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(첫번째_테이블, 두번째_테이블));

        //When + Then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있지 않은 경우, 예외가 발생한다")
    @Test
    void 주문테이블이_비어있지_않은_경우_예외발생() {
        //Given
        첫번째_테이블.setEmpty(false);
        테이블그룹.setOrderTables(Arrays.asList(첫번째_테이블, 두번째_테이블));
        when(orderTableRepository.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(첫번째_테이블, 두번째_테이블));

        //When + Then
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 취소할 수 있다")
    @Test
    void 단체지정_취소() {
        //Given
        List<OrderTable> 테이블_목록 = new ArrayList<>(Arrays.asList(첫번째_테이블, 두번째_테이블));
        List<Long> 테이블_ID_목록 = 테이블_목록.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        when(orderTableRepository.findAllByTableGroupId(테이블그룹.getId())).thenReturn(테이블_목록);
        when(orderRepository.existsByOrderTableIdInAndOrderStatusIn(테이블_ID_목록,Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);

        //When
        tableGroupService.ungroup(테이블그룹.getId());

        //Then
        assertThat(첫번째_테이블.getTableGroup()).isNull();
        assertThat(두번째_테이블.getTableGroup()).isNull();
    }
}
