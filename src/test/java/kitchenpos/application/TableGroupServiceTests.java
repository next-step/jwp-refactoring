package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

@DisplayName("테이블그룹 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTests {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private TableGroup 테이블그룹;
    private List<OrderTable> 주문테이블_목록;
    private List<Long> 주문테이블_아이디_목록;

    @BeforeEach
    public void setUp() {
        주문테이블1 = new OrderTable.builder()
                .id(1L)
                .numberOfGuests(3)
                .tableGroupId(1L)
                .empty(true)
                .build();
        주문테이블2 = new OrderTable.builder()
                .id(2L)
                .numberOfGuests(5)
                .tableGroupId(1L)
                .empty(true)
                .build();
        주문테이블_목록 = Arrays.asList(주문테이블1, 주문테이블2);
        주문테이블_아이디_목록 = Arrays.asList(주문테이블1.getId(), 주문테이블2.getId());
        테이블그룹 = new TableGroup.builder()
                .id(1L)
                .orderTables(Arrays.asList(주문테이블1, 주문테이블2))
                .build();
    }

    @Test
    public void 테이블그룹_생성() {
        OrderTable 주문테이블1 = new OrderTable.builder()
                .id(1L)
                .numberOfGuests(3)
                .empty(true)
                .build();
        OrderTable 주문테이블2 = new OrderTable.builder()
                .id(2L)
                .numberOfGuests(5)
                .empty(true)
                .build();
        TableGroup 테이블그룹 = new TableGroup.builder()
                .id(1L)
                .orderTables(Arrays.asList(주문테이블1, 주문테이블2))
                .build();

        lenient().when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        lenient().when(tableGroupDao.save(테이블그룹))
                .thenReturn(테이블그룹);

        assertThat(tableGroupService.create(테이블그룹))
                .isNotNull()
                .isInstanceOf(TableGroup.class)
                .isEqualTo(테이블그룹);
    }

    @Test
    public void 주문테이블이_없는_경우_생성실패() {
        TableGroup 테이블그룹 = new TableGroup.builder()
                .id(1L)
                .build();

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 2개이상 필요합니다.");
    }

    @Test
    public void 저장된_주문테이블수가_일치하지_않을_경우_생성실패() {
        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블 수가 일치하지 않습니다.");
    }

    @Test
    public void 존재하지_않는_주문테이블포함일_경우_생성실패() {
        lenient().when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId())))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블에 이미 테이블그룹번호가 존재합니다.");
    }

    @Test
    public void 테이블그룹_해제() {
        lenient().when(orderTableDao.findAllByTableGroupId(1L))
                .thenReturn(주문테이블_목록);
        lenient().when(orderDao.existsByOrderTableIdInAndOrderStatusIn(주문테이블_아이디_목록, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);
        주문테이블_목록.forEach(orderTable -> {
            orderTable.setTableGroupId(null);
            lenient().when(orderTableDao.save(orderTable))
                    .thenReturn(orderTable);
        });

        tableGroupService.ungroup(주문테이블1.getId());
    }

    @Test
    public void 테이블그룹_해제실패() {
        lenient().when(orderTableDao.findAllByTableGroupId(1L))
                .thenReturn(주문테이블_목록);
        lenient().when(orderDao.existsByOrderTableIdInAndOrderStatusIn(주문테이블_아이디_목록, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
