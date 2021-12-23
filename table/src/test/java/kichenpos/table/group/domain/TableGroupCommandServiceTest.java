package kichenpos.table.group.domain;

import static kichenpos.table.group.sample.TableGroupSample.두명_세명_테이블_그룹;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("테이블 그룹 커맨드 서비스")
@SuppressWarnings("NonAsciiCharacters")
class TableGroupCommandServiceTest {

    @Mock
    private TableGroupRepository groupRepository;

    @InjectMocks
    private TableGroupCommandService commandService;


    @Test
    @DisplayName("단체 저장")
    void save() {
        //given
        TableGroup 두명_세명_테이블_그룹 = 두명_세명_테이블_그룹();

        //when
        commandService.save(두명_세명_테이블_그룹);

        //then
        verify(groupRepository, only()).save(두명_세명_테이블_그룹);
    }

    @Test
    @DisplayName("단체 지정 해제")
    void ungroup() {
        //given
        long tableGroupId = 1L;
        TableGroup tableGroup = mock(TableGroup.class);
        when(groupRepository.tableGroup(tableGroupId)).thenReturn(tableGroup);

        //when
        commandService.ungroup(tableGroupId);

        //then
        verify(tableGroup, only()).ungroup();
    }
}

