package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.fixture.UnitTestFixture;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.TableService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableService tableService;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private TableGroupService tableGroupService;

    private UnitTestFixture 식당_포스;

    @BeforeEach
    void setUp() {
        식당_포스 = new UnitTestFixture();
    }

    @Test
    void 단체_지정을_할_수_있어야_한다() {
        // given
        final TableGroupRequest given = new TableGroupRequest(Arrays.asList(식당_포스.빈_테이블1.getId(), 식당_포스.빈_테이블2.getId()));
        final TableGroup expected = new TableGroup(1L);
        when(tableService.getById(식당_포스.빈_테이블1.getId())).thenReturn(식당_포스.빈_테이블1);
        when(tableService.getById(식당_포스.빈_테이블2.getId())).thenReturn(식당_포스.빈_테이블2);
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(expected);

        // when
        final TableGroupResponse actual = tableGroupService.create(given);

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    void 단체_지정을_해제할_수_있어야_한다() {
        // given
        when(tableGroupRepository.findById(any(Long.class))).thenReturn(Optional.of(식당_포스.단체));

        // when
        tableGroupService.ungroup(식당_포스.단체.getId());

        // then
        assertThat(식당_포스.단체_지정_테이블1.getTableGroupId()).isNull();
        assertThat(식당_포스.단체_지정_테이블2.getTableGroupId()).isNull();
    }

    @Test
    void 단체_지정_해제_시_조리_또는_식사_중인_테이블이_있으면_에러가_발생해야_한다() {
        // given
        when(tableGroupRepository.findById(any(Long.class))).thenReturn(Optional.of(식당_포스.단체));
        when(orderService.existsNotCompletesByOrderTableIdIn(anyList())).thenReturn(true);

        // when and then
        assertThatThrownBy(() -> tableGroupService.ungroup(식당_포스.단체.getId()))
                .isInstanceOf(IllegalStateException.class);
    }
}
