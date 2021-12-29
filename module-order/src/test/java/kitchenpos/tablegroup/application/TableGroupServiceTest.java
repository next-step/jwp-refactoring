package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 테이블 관리 기능")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private TableGroupValidator tableGroupValidator;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("`단체 지정`은 등록 할 수 있다.")
    void 단체지정_등록() {
        // given
        TableGroup 단체지정 = TableGroup.of(1L);
        TableGroupRequest 요청_파라미터 = new TableGroupRequest(Arrays.asList(1L, 2L));
        given(tableGroupRepository.save(any())).willReturn(단체지정);

        // when
        TableGroupResponse 등록된_단체지정 = tableGroupService.create(요청_파라미터);

        // then
        assertThat(등록된_단체지정).isNotNull();
    }

    @Test
    @DisplayName("`단체 지정` 해지할 수 있다.")
    void 단체지정_해지() {
        // given
        TableGroup 단체지정 = TableGroup.of(1L);
        given(tableGroupRepository.findById(any())).willReturn(Optional.of(단체지정));

        // when
        tableGroupService.ungroup(1L);
    }
}
