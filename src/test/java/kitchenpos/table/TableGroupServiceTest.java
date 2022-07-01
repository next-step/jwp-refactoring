package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    TableGroup 단체손님;
    OrderTable 주문테이블1;
    OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = new OrderTable(2, true);
        주문테이블2 = new OrderTable(2, true);

        단체손님 = new TableGroup(Arrays.asList(주문테이블1, 주문테이블2));
    }

    @Test
    @DisplayName("단체를 지정하여 저장한다")
    void create() {
        // given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(단체손님.getOrderTables());
        given(tableGroupRepository.save(any())).willReturn(단체손님);

        // when
        TableGroup actual = tableGroupService.create(단체손님);

        // then
        assertThat(actual.getOrderTables()).isNotNull();
    }

    @Test
    @DisplayName("단체 지정시 최소 2팀 이상 지정해야 한다")
    void create_singleTableGroupError() {
        // given & when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(단체손님)
        );
    }

    @Test
    @DisplayName("단체 지정시 유효한 주문 테이블 정보를 지정해야 한다")
    void create_orderTableGroupError() {
        // given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Collections.singletonList(주문테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(단체손님)
        );
    }

    @Test
    @DisplayName("단체 지정시 주문 테이블은 단체 테이블 정보를 가지고 있다")
    void create_emptyTableGroupError() {
        // given
        주문테이블1.setEmpty(false);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(단체손님.getOrderTables());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(단체손님)
        );
    }

    @Test
    @DisplayName("지정한 단체를 해제한다")
    void ungroup() {
        // given
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        tableGroupService.ungroup(단체손님.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroup()).isNull(),
                () -> assertThat(주문테이블2.getTableGroup()).isNull()
        );
    }

    @Test
    @DisplayName("지정한 단체를 해제시 주문 상태가 계산 완료여야 한다")
    void ungroup_completionError() {
        // given
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.ungroup(단체손님.getId())
        );
    }
}
