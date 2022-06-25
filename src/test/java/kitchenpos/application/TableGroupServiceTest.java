package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.fixture.TableFixture.*;
import static kitchenpos.fixture.TableGroupFixture.단체_주문_테이블_그룹;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Test
    @DisplayName("테이블 그룹 생성 성공 테스트")
    void create() {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(단체_주문_테이블_4명, 단체_주문_테이블_6명));
        when(tableGroupDao.save(any())).thenReturn(단체_주문_테이블_그룹);
        when(orderTableDao.save(any())).thenReturn(단체_주문_테이블_4명);
        when(orderTableDao.save(any())).thenReturn(단체_주문_테이블_6명);

        TableGroup 테이블_그룹_생성_결과 = tableGroupService.create(단체_주문_테이블_그룹);

        Assertions.assertThat(테이블_그룹_생성_결과).isEqualTo(단체_주문_테이블_그룹);
    }

    @Test
    @DisplayName("테이블 그룹 생성시 테이블 개수가 1개인 경우 실패 테스트")
    void create2() {
        TableGroup 메뉴_개수_1개 = new TableGroup();
        메뉴_개수_1개.setOrderTables(Arrays.asList(단체_주문_테이블_4명));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(메뉴_개수_1개));
    }

    @Test
    @DisplayName("테이블 그룹 생성시 테이블 개수와 조회된 개수가 서로 다른 경우 실패 테스트")
    void create3() {
        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(단체_주문_테이블_6명));

        TableGroup 메뉴_개수_2개 = new TableGroup();
        메뉴_개수_2개.setOrderTables(Arrays.asList(단체_주문_테이블_4명, 단체_주문_테이블_6명));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(메뉴_개수_2개));
    }

    @Test
    @DisplayName("테이블 그룹 생성시 테이블내 비어있지 않은 경우 실패 테스트")
    void create4() {
        TableGroup 테이블_그룹이_비어있지_않음 = new TableGroup();
        테이블_그룹이_비어있지_않음.setOrderTables(Arrays.asList(단체_주문_테이블_4명, 단체_주문_테이블_6명));

        when(orderTableDao.findAllByIdIn(anyList())).thenReturn(Arrays.asList(단체_주문_테이블_4명, 주문_테이블_여섯이_있음));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(테이블_그룹이_비어있지_않음));
    }

    @Test
    @DisplayName("테이블 그룹 해체 성공 테스트")
    void ungroup() {
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(단체_주문_테이블_4명, 단체_주문_테이블_6명));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);

        tableGroupService.ungroup(단체_주문_테이블_그룹.getId());

        assertAll(
                () -> Assertions.assertThat(단체_주문_테이블_4명.getTableGroupId()).isNull(),
                () -> Assertions.assertThat(단체_주문_테이블_6명.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("테이블 그룹 해체시 테이블내 주문 상태가 식사, 요리 상태인 경우 실패 테스트")
    void ungroup2() {
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(단체_주문_테이블_4명, 단체_주문_테이블_6명));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).thenReturn(false);

        tableGroupService.ungroup(단체_주문_테이블_그룹.getId());

        assertAll(
                () -> Assertions.assertThat(단체_주문_테이블_4명.getTableGroupId()).isNull(),
                () -> Assertions.assertThat(단체_주문_테이블_6명.getTableGroupId()).isNull()
        );
    }
}
