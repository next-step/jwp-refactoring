package kitchenpos.table;

import static kitchenpos.table.TableGroupFixture.createTableGroupRequest;
import static kitchenpos.table.TableGroupFixture.단체지정;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.repository.TableGroupRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.validator.TableGroupValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupValidator validator;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private ApplicationEventPublisher publisher;
    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable 빈테이블;

    @BeforeEach
    void setup() {
        빈테이블 = new OrderTable(2L, 0, true);
    }

    @Test
    @DisplayName("테이블 그룹 생성 성공")
    void createTableGroup() {
        //given
        when(tableGroupRepository.save(any())).then(returnsFirstArg());
        TableGroupRequest tableGroupRequest = createTableGroupRequest(단체지정);

        //when & then
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);
    }

    @Test
    @DisplayName("단체 지정 해제")
    void ungroup() {
        //given
        TableGroup 단체지정 = new TableGroup(1L);
        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(단체지정));

        //when & then
        tableGroupService.ungroup(1L);
    }
}
