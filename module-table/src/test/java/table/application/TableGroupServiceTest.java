package table.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import table.domain.OrderTable;
import table.domain.TableGroup;
import table.dto.TableGroupRequest;
import table.dto.TableGroupResponse;
import table.repository.OrderTableRepository;
import table.repository.TableGroupRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableGroupValidator tableGroupValidator;

    private TableGroup 테이블_그룹;
    private OrderTable 첫번째_테이블;
    private OrderTable 두번째_테이블;
    private OrderTable 사용중인_테이블;

    @BeforeEach
    void setUp() {
        테이블_그룹 = new TableGroup(1L);
        첫번째_테이블 = new OrderTable(1L, null, 2, true);
        두번째_테이블 = new OrderTable(2L, null, 3, true);
        사용중인_테이블 = new OrderTable(3L, 테이블_그룹, 5, false);
    }

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_테이블, 두번째_테이블);

        TableGroupRequest request = new TableGroupRequest(Arrays.asList(1L, 2L));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(테이블_그룹);

        // when
        TableGroupResponse response = tableGroupService.create(request);

        // then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(1L),
                () -> assertThat(response.getOrderTables()).hasSize(2)
        );
    }

    @Test
    void 주문_테이블이_2개_미만일_경우() {
        // given
        TableGroupRequest request = new TableGroupRequest(Collections.singletonList(1L));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블일_경우() {
        // given
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(1L, 2L));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Collections.singletonList(첫번째_테이블));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블이_아닐경우() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_테이블, 사용중인_테이블);
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(1L, 3L));
        given(orderTableRepository.findAllByIdIn(any())).willReturn(orderTables);
        given(tableGroupRepository.save(any())).willReturn(테이블_그룹);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(첫번째_테이블, 두번째_테이블);
        테이블_그룹.addOrderTables(orderTables);
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(테이블_그룹));
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(orderTables);

        // when
        tableGroupService.ungroup(테이블_그룹.getId());

        // then
        assertAll(
                () -> assertThat(첫번째_테이블.getTableGroup()).isNull(),
                () -> assertThat(두번째_테이블.getTableGroup()).isNull()
        );
    }
}
