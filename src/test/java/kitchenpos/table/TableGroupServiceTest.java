package kitchenpos.table;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    TableGroup 단체손님;
    OrderTable 주문테이블1;
    OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = new OrderTable();
        주문테이블1.setEmpty(true);

        주문테이블2 = new OrderTable();
        주문테이블2.setEmpty(true);

        단체손님 = new TableGroup();
        단체손님.setId(1L);
    }

    @Test
    @DisplayName("단체를 지정하여 저장한다")
    void create() {
        // given
        단체손님.setOrderTables(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(단체손님.getOrderTables());
        given(tableGroupDao.save(단체손님)).willReturn(단체손님);

        // when
        TableGroup actual = tableGroupService.create(단체손님);

        // then
        assertThat(actual).isEqualTo(단체손님);
    }

    @Test
    @DisplayName("단체 지정시 최소 2팀 이상 지정해야 한다")
    void create_singleTablegroupError() {
        // given
        단체손님.setOrderTables(Collections.singletonList(주문테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(단체손님)
        );
    }

    @Test
    @DisplayName("단체 지정시 유효한 주문 테이블 정보를 지정해야 한다")
    void create_orderTableGroupError() {
        // given
        단체손님.setOrderTables(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.singletonList(주문테이블1));

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
        단체손님.setOrderTables(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(단체손님.getOrderTables());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(단체손님)
        );
    }

    @Test
    @DisplayName("지정한 단체를 해제한다")
    void ungroup() {
        // given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        tableGroupService.ungroup(단체손님.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroupId()).isNull(),
                () -> assertThat(주문테이블2.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("지정한 단체를 해제시 주문 상태가 계산 완료여야 한다")
    void ungroup_completionError() {
        // given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.ungroup(단체손님.getId())
        );
    }
}
