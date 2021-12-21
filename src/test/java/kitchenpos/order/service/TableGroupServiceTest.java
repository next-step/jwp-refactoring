package kitchenpos.order.service;

import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.order.OrderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroup 요청_테이블_그룹;

    private OrderTable 주문_테이블_일번;
    private OrderTable 주문_테이블_이번;

    @BeforeEach
    void setUp() {
        주문_테이블_일번 = OrderFactory.ofOrderTable(1L, null, true, 4);
        주문_테이블_이번 = OrderFactory.ofOrderTable(2L, null, true, 3);
        요청_테이블_그룹 = OrderFactory.ofTableGroup(1L, Arrays.asList(주문_테이블_일번, 주문_테이블_이번), null);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void 테이블_그룹_생성() {
        TableGroup 저장된_테이블_그룹 = OrderFactory.ofTableGroup(1L, Arrays.asList(주문_테이블_일번, 주문_테이블_이번), LocalDateTime.now());
        ;
        given(orderTableDao.findAllByIdIn(Arrays.asList(요청_테이블_그룹.getId(), 주문_테이블_이번.getId())))
                .willReturn(Arrays.asList(주문_테이블_일번, 주문_테이블_이번));
        given(tableGroupDao.save(요청_테이블_그룹)).willReturn(저장된_테이블_그룹);
        given(orderTableDao.save(주문_테이블_일번)).willReturn(주문_테이블_일번);
        given(orderTableDao.save(주문_테이블_이번)).willReturn(주문_테이블_이번);

        TableGroup response = tableGroupService.create(요청_테이블_그룹);

        assertAll(
                () -> assertThat(response.getCreatedDate()).isEqualTo(저장된_테이블_그룹.getCreatedDate()),
                () -> assertThat(주문_테이블_일번.getTableGroupId()).isEqualTo(요청_테이블_그룹.getId()),
                () -> assertThat(주문_테이블_일번.isEmpty()).isFalse(),
                () -> assertThat(주문_테이블_이번.getTableGroupId()).isEqualTo(요청_테이블_그룹.getId()),
                () -> assertThat(주문_테이블_이번.isEmpty()).isFalse()
        );
    }

    @DisplayName("테이블 그룹을 생성 요청 받은 주문 테이블이 최소 2개 이상이어야 한다.")
    @Test
    void 테이블_그룹_생성_주문_테이블_2개_미만_예외() {
        요청_테이블_그룹.setOrderTables(Collections.singletonList(주문_테이블_일번));

        Throwable thrown = catchThrowable(() -> tableGroupService.create(요청_테이블_그룹));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성 요청 받은 주문 테이블들이 디비에 존재해야 한다.")
    @Test
    void 테이블_그룹_생성_주문_테이블_미존재_예외() {
        given(orderTableDao.findAllByIdIn(Arrays.asList(요청_테이블_그룹.getId(), 주문_테이블_이번.getId())))
                .willReturn(Collections.singletonList(주문_테이블_일번));

        Throwable thrown = catchThrowable(() -> tableGroupService.create(요청_테이블_그룹));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성 요청 받은 주문 테이블들 빈테이블 아님 예외")
    @Test
    void 테이블_그룹_생성_주문_테이블들_빈테이블_아님_예외() {
        주문_테이블_일번.setEmpty(false);
        given(orderTableDao.findAllByIdIn(Arrays.asList(요청_테이블_그룹.getId(), 주문_테이블_이번.getId())))
                .willReturn(Arrays.asList(주문_테이블_일번, 주문_테이블_이번));

        Throwable thrown = catchThrowable(() -> tableGroupService.create(요청_테이블_그룹));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 생성 요청 받은 주문 테이블들 이미 그룹이 존재함 예외")
    @Test
    void 테이블_그룹_생성_주문_테이블들_이미_그룹_존재_예외() {
        주문_테이블_일번.setTableGroupId(3L);
        given(orderTableDao.findAllByIdIn(Arrays.asList(요청_테이블_그룹.getId(), 주문_테이블_이번.getId())))
                .willReturn(Arrays.asList(주문_테이블_일번, 주문_테이블_이번));

        Throwable thrown = catchThrowable(() -> tableGroupService.create(요청_테이블_그룹));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹_해제() {
        given(orderTableDao.findAllByTableGroupId(요청_테이블_그룹.getId()))
                .willReturn(Arrays.asList(주문_테이블_일번, 주문_테이블_이번));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문_테이블_일번.getId(), 주문_테이블_이번.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(주문_테이블_일번)).willReturn(주문_테이블_일번);
        given(orderTableDao.save(주문_테이블_이번)).willReturn(주문_테이블_이번);

        tableGroupService.ungroup(요청_테이블_그룹.getId());

        assertAll(
                () -> assertThat(주문_테이블_일번.getTableGroupId()).isNull(),
                () -> assertThat(주문_테이블_이번.getTableGroupId()).isNull()
        );
    }

    @DisplayName("주문들의 상태가 완료가 아닌 경우 그룹을 해제할 수 없다.")
    @Test
    void 주문_상태_완료_아님_예외() {
        given(orderTableDao.findAllByTableGroupId(요청_테이블_그룹.getId()))
                .willReturn(Arrays.asList(주문_테이블_일번, 주문_테이블_이번));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문_테이블_일번.getId(), 주문_테이블_이번.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        Throwable thrown = catchThrowable(() -> tableGroupService.ungroup(요청_테이블_그룹.getId()));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
    }
}
