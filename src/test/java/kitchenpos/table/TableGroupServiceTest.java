package kitchenpos.table;

import static kitchenpos.table.TableGroupFixture.createTableGroupRequest;
import static kitchenpos.table.TableGroupFixture.단체지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 빈테이블;

    @BeforeEach
    void setup() {
        빈테이블 = new OrderTable(2L, 0, true, Collections.emptyList());
    }

    @Test
    @DisplayName("테이블 그룹 생성 성공")
    void createTableGroup() {
        //given
        when(orderTableRepository.findAllById(any())).thenReturn(Arrays.asList(빈테이블, 빈테이블));
        when(tableGroupRepository.save(any())).then(returnsFirstArg());
        TableGroupRequest tableGroupRequest = createTableGroupRequest(단체지정);

        //when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(tableGroupResponse.getOrderTables())
            .hasSize(2)
            .extracting(OrderTableResponse::isEmpty)
            .containsExactly(false, false);
    }

    @Test
    @DisplayName("테이블이 없어서 그룹 생성 실패")
    void noTableException() {
        //given
        when(orderTableRepository.findAllById(any())).thenReturn(Collections.emptyList());
        TableGroupRequest tableGroupRequest = createTableGroupRequest(단체지정);

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("단체 지정할 테이블 중 존재하지 않는 테이블이 존재 합니다.");
    }

    @Test
    @DisplayName("단체 지정 해제")
    void ungroup() {
        //given
        TableGroup 단체지정 = new TableGroup(1L, Arrays.asList(빈테이블, 빈테이블));
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(단체지정));

        //when & then
        tableGroupService.ungroup(1L);
    }

    @Test
    @DisplayName("단체 지정이 없으면 에러 발생")
    void noGroupException() {
        //given
        when(tableGroupRepository.findById(any())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 테이블 그룹 입니다.");
    }


}
