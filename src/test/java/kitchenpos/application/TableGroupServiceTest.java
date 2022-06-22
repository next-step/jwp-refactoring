package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.application.TableServiceTest.주문_테이블_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("단체 주문 테이블 관련 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, null, 2, true);
        OrderTable orderTableId2 = 주문_테이블_데이터_생성(2L, null, 4, true);
        List<OrderTable> orderTables = Arrays.asList(orderTableId1, orderTableId2);
        TableGroup request = 테이블_그룹_데이터_생성(null, LocalDateTime.now(), orderTables);
        TableGroup 예상값 = 테이블_그룹_데이터_생성(1L, LocalDateTime.now(), orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);
        given(tableGroupDao.save(request)).willReturn(예상값);

        // when
        TableGroup 테이블_그룹_생성_결과 = 테이블_그룹_생성(request);

        // then
        테이블_그룹_데이터_비교(테이블_그룹_생성_결과, 예상값);
    }

    @DisplayName("테이블 그룹 생성 - 주문 테이블이 빈 테이블이 아닐 경우")
    @Test
    void create_exception1() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, null, 2, false);
        OrderTable orderTableId2 = 주문_테이블_데이터_생성(2L, null, 4, false);
        List<OrderTable> orderTables = Arrays.asList(orderTableId1, orderTableId2);
        TableGroup request = 테이블_그룹_데이터_생성(null, LocalDateTime.now(), orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        // when && then
        assertThatThrownBy(() -> 테이블_그룹_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 주문 테이블의 수가 2개 미만일 경우")
    @Test
    void create_exception2() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, null, 2, false);
        List<OrderTable> orderTables = Collections.singletonList(orderTableId1);
        TableGroup request = 테이블_그룹_데이터_생성(null, LocalDateTime.now(), orderTables);

        // when && then
        assertThatThrownBy(() -> 테이블_그룹_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 생성 - 주문 테이블이 이미 그룹이 있는 경우")
    @Test
    void create_exception3() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, null, 2, false);
        OrderTable orderTableId2 = 주문_테이블_데이터_생성(2L, 1L, 4, false);
        List<OrderTable> orderTables = Arrays.asList(orderTableId1, orderTableId2);
        TableGroup request = 테이블_그룹_데이터_생성(null, LocalDateTime.now(), orderTables);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        // when && then
        assertThatThrownBy(() -> 테이블_그룹_생성(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, 1L, 2, true);
        OrderTable orderTableId2 = 주문_테이블_데이터_생성(2L, 1L, 4, true);
        List<OrderTable> orderTables = Arrays.asList(orderTableId1, orderTableId2);

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        // when & then
        테이블_그룹_해제(1L);
    }

    @DisplayName("테이블 그룹 해제 - 주문 상태가 '조리' 또는 '식사' 상태일 경우")
    @Test
    void ungroup_exception1() {
        // given
        OrderTable orderTableId1 = 주문_테이블_데이터_생성(1L, 1L, 2, true);
        OrderTable orderTableId2 = 주문_테이블_데이터_생성(2L, 1L, 4, true);
        List<OrderTable> orderTables = Arrays.asList(orderTableId1, orderTableId2);

        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> 테이블_그룹_해제(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private TableGroup 테이블_그룹_데이터_생성(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        return new TableGroup(id, createdDate, orderTables);
    }

    private TableGroup 테이블_그룹_생성(TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

    private void 테이블_그룹_해제(long l) {
        tableGroupService.ungroup(l);
    }

    private void 테이블_그룹_데이터_비교(TableGroup 테이블_그룹_생성_결과, TableGroup 예상값) {
        assertAll(
                () -> assertThat(테이블_그룹_생성_결과.getId()).isEqualTo(예상값.getId()),
                () -> assertThat(테이블_그룹_생성_결과.getOrderTables()).isEqualTo(예상값.getOrderTables())
        );
    }
}
