package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.common.error.InvalidRequestException;
import kitchenpos.order.domain.NumberOfGuests;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.repository.OrderDao;
import kitchenpos.order.repository.OrderTableDao;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupDao;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 지정 테스트")
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable orderTable;
    private OrderTable addOrderTable;

    @BeforeEach
    void setup() {
        orderTable = new OrderTable(1L, new NumberOfGuests(2), true);
        addOrderTable = new OrderTable(2L, new NumberOfGuests(2), true);
    }

    @DisplayName("사용자는 단체 지정을 할 수 있다.")
    @Test
    void create() {
        // given

        // when
        when(orderTableDao.findAllById(any())).thenReturn(Arrays.asList(orderTable, addOrderTable));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);

        TableGroupResponse createdTableGroup = tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)));
        // then
        assertThat(createdTableGroup).isNotNull();
    }

    @DisplayName("사용자는 단체를 취소 할 수 있다.")
    @Test
    void ungroup() {
        // given
        // when
        when(tableGroupDao.findById(any())).thenReturn(Optional.of(new TableGroup()));
        // then
        tableGroupService.ungroup(1L);
    }

    @DisplayName("주문테이블의 요청 id의 개수가 2보다 작은지 체크한다.")
    @Test
    void createFailedByOrderTables() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest(Arrays.asList(1L))))
                .isInstanceOf(InvalidRequestException.class);
    }

    @DisplayName("주문테이블의 데이터를 체크한다. 이 때 요청 받은 주문테이블의 데이터가 모두 있는지 체크한다.")
    @Test
    void createFailedByOrderTablesCount() {
        // given
        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupRequest()))
                .isInstanceOf(InvalidRequestException.class);
    }
}