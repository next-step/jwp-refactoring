package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.fixture.TableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupHandlerTest {

    @InjectMocks
    private TableGroupHandler tableGroupHandler;

    @Mock
    private OrderTableRepository orderTableRepository;

    private Long 테이블_그룹_ID = 1L;
    private OrderTable 비어있는_테이블1;
    private OrderTable 비어있는_테이블2;
    private OrderTable 테이블_그룹에_속해있는_테이블1;
    private OrderTable 테이블_그룹에_속해있는_테이블2;

    @BeforeEach
    public void setUp() {
        비어있는_테이블1 = TableFixture.create(1L, null, 0, true);
        비어있는_테이블2 = TableFixture.create(2L, null, 0, true);
        테이블_그룹에_속해있는_테이블1 = TableFixture.create(1L, 1L, 0, true);
        테이블_그룹에_속해있는_테이블2 = TableFixture.create(2L, 1L, 0, true);
    }

    @DisplayName("테이블 그룹화 성공")
    @Test
    void group_success() {
        // given
        given(orderTableRepository.findAllByIdIn(Arrays.asList(비어있는_테이블1.getId(), 비어있는_테이블2.getId())))
                .willReturn(Arrays.asList(비어있는_테이블1, 비어있는_테이블2));

        // when
        tableGroupHandler.group(new TableGroupEvent(테이블_그룹_ID, Arrays.asList(비어있는_테이블1.getId(), 비어있는_테이블2.getId())));

        // then
        assertAll(
                () -> assertThat(비어있는_테이블1.getTableGroupId()).isEqualTo(테이블_그룹_ID)
                , () -> assertThat(비어있는_테이블2.getTableGroupId()).isEqualTo(테이블_그룹_ID)
        );
    }

    @DisplayName("테이블 그룹화 실패 - 테이블 수 일치하지 않음")
    @Test
    void group_failure_invalidOrderTableSize() {
        // given
        Long tableGroupId = 1L;
        given(orderTableRepository.findAllByIdIn(Arrays.asList(비어있는_테이블1.getId(), 비어있는_테이블2.getId())))
                .willReturn(Arrays.asList(비어있는_테이블1));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupHandler.group(new TableGroupEvent(tableGroupId, Arrays.asList(비어있는_테이블1.getId(), 비어있는_테이블2.getId()))));
    }

    @DisplayName("테이블 그룹화 해제 성공")
    @Test
    void ungroup_success() {
        // given
        given(orderTableRepository.findAllByTableGroupId(테이블_그룹_ID))
                .willReturn(Arrays.asList(테이블_그룹에_속해있는_테이블1, 테이블_그룹에_속해있는_테이블2));

        // when
        tableGroupHandler.ungroup(new TableUngroupEvent(테이블_그룹_ID));

        // then
        assertAll(
                () -> assertThat(비어있는_테이블1.getTableGroupId()).isNull()
                , () -> assertThat(비어있는_테이블2.getTableGroupId()).isNull()
        );
    }
}
