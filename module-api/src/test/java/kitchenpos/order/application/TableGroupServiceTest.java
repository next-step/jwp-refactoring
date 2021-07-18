package kitchenpos.order.application;

import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Mockito.when(tableGroupRepository.save(ArgumentMatchers.any())).thenReturn(단체지정);

        //When
        tableGroupService.create(단체지정_요청);

        //Then
        Mockito.verify(tableGroupRepository, Mockito.times(1)).save(ArgumentMatchers.any());
    }

    @DisplayName("단체 지정을 취소할 수 있다")
    @Test
    void 단체지정_취소() {
        //Given
        TableGroup 단체지정 = new TableGroup(1L);

        Mockito.when(tableGroupRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(단체지정));

        //When
        tableGroupService.ungroup(단체지정.getId());

        //Then
        Mockito.verify(tableGroupRepository, Mockito.times(1)).findById(ArgumentMatchers.any());
    }
}
