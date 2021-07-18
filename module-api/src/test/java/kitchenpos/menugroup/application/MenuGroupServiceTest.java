package kitchenpos.menugroup.application;

import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.menugroup.MenuGroupTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void 메뉴그룹_등록() {
        //Given
        when(menuGroupRepository.save(any())).thenReturn(맥모닝_메뉴그룹);

        //When
        MenuGroupResponse 생성된_메뉴그룹 = menuGroupService.create(맥모닝_메뉴그룹_요청);

        //Then
        verify(menuGroupRepository, times(1)).save(any());
        assertThat(생성된_메뉴그룹.getName()).isEqualTo(맥모닝_메뉴그룹_요청.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void 메뉴그룹_목록_조회() {
        //Given
        List<MenuGroup> 입력한_메뉴그룹_목록 = Arrays.asList(맥모닝_메뉴그룹, 맥디너_메뉴그룹);
        when(menuGroupRepository.findAll()).thenReturn(입력한_메뉴그룹_목록);

        //When
        List<MenuGroupResponse> 조회된_메뉴그룹_목록 = menuGroupService.list();

        //Then
        verify(menuGroupRepository, times(1)).findAll();
        Assertions.assertThat(조회된_메뉴그룹_목록).isNotNull()
                .hasSize(입력한_메뉴그룹_목록.size());
    }
}
