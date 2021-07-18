package kitchenpos.tablegroup.application;

import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupValidator tableGroupValidator;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체지정 = new TableGroup();

    @DisplayName("단체 지정을 등록한다")
    @Test
    void 단체지정_등록() {
        //Given
        TableGroupRequest 단체지정_요청 = new TableGroupRequest(1L, LocalDateTime.now(), Arrays.asList(1L, 2L));
        when(tableGroupRepository.save(any())).thenReturn(단체지정);

        //When
        tableGroupService.create(단체지정_요청);

        //Then
        verify(tableGroupRepository, times(1)).save(any());
    }

    @DisplayName("단체 지정을 취소할 수 있다")
    @Test
    void 단체지정_취소() {
        //Given
        TableGroup 단체지정 = new TableGroup(1L);

        when(tableGroupRepository.findById(any())).thenReturn(Optional.of(단체지정));

        //When
        tableGroupService.ungroup(단체지정.getId());

        //Then
        verify(tableGroupRepository, times(1)).findById(any());
    }
}
