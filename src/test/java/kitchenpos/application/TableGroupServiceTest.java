package kitchenpos.application;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.주문_테이블_데이터_생성;
import static kitchenpos.fixture.TableGroupFixture.테이블_그룹_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

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

    private TableGroup 테이블_그룹;
    private OrderTable 첫번째_테이블;
    private OrderTable 두번째_테이블;
    private OrderTable 사용_테이블;

    @BeforeEach
    void setUp() {
        첫번째_테이블 = 주문_테이블_데이터_생성(1L, null, 2, true);
        두번째_테이블 = 주문_테이블_데이터_생성(2L, null, 3, true);
        사용_테이블 = 주문_테이블_데이터_생성(2L, 2L, 5, false);
    }

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_테이블, 두번째_테이블);
        테이블_그룹 = 테이블_그룹_데이터_생성(1L, null, orderTables);
        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);
        given(tableGroupDao.save(any())).willReturn(테이블_그룹);
        given(orderTableDao.save(any())).willReturn(첫번째_테이블, 두번째_테이블);

        // when
        TableGroup tableGroup = tableGroupService.create(테이블_그룹);

        // then
        assertAll(
                () -> assertThat(tableGroup).isEqualTo(테이블_그룹),
                () -> assertThat(tableGroup.getOrderTables()).containsExactly(첫번째_테이블, 두번째_테이블)
        );
    }

    @Test
    void 주문_테이블이_2개_미만일_경우() {
        // given
        List<OrderTable> orderTables = Collections.singletonList(첫번째_테이블);
        테이블_그룹 = 테이블_그룹_데이터_생성(1L, null, orderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블일_경우() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_테이블, 두번째_테이블);
        테이블_그룹 = 테이블_그룹_데이터_생성(1L, null, orderTables);
        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.singletonList(첫번째_테이블));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블이_아닐경우() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_테이블, 사용_테이블);
        테이블_그룹 = 테이블_그룹_데이터_생성(1L, null, orderTables);
        given(orderTableDao.findAllByIdIn(any())).willReturn(orderTables);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(테이블_그룹))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_테이블, 두번째_테이블);
        테이블_그룹 = 테이블_그룹_데이터_생성(1L, null, orderTables);
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        // when
        tableGroupService.ungroup(테이블_그룹.getId());

        // then
        assertAll(
                () -> assertThat(첫번째_테이블.getTableGroupId()).isNull(),
                () -> assertThat(두번째_테이블.getTableGroupId()).isNull()
        );
    }

    @Test
    void 주문_상태가_조리_또는_식사중인_테이블을_해제하는_경우() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_테이블, 두번째_테이블);
        테이블_그룹 = 테이블_그룹_데이터_생성(1L, null, orderTables);
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
