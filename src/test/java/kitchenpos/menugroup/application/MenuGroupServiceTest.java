package kitchenpos.menugroup.application;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

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

    private MenuGroupRequest 첫번째_메뉴그룹_요청 = new MenuGroupRequest("첫번째_메뉴그룹");
    private MenuGroup 첫번째_메뉴그룹 = new MenuGroup(1L, "첫번째_메뉴그룹");
    private MenuGroup 두번째_메뉴그룹 = new MenuGroup(2L, "두번째_메뉴그룹");
    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void 메뉴그룹_등록() {
        //Given
        when(menuGroupRepository.save(any())).thenReturn(첫번째_메뉴그룹_요청.toMenuGroup());

        //When
        MenuGroupResponse 생성된_메뉴그룹 = menuGroupService.create(첫번째_메뉴그룹_요청);

        //Then
        verify(menuGroupRepository, times(1)).save(any());
        assertThat(생성된_메뉴그룹.getName()).isEqualTo(첫번째_메뉴그룹_요청.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void 메뉴그룹_목록_조회() {
        //Given
        List<MenuGroup> 입력한_메뉴그룹_목록 = Arrays.asList(첫번째_메뉴그룹, 두번째_메뉴그룹);
        when(menuGroupRepository.findAll()).thenReturn(입력한_메뉴그룹_목록);

        //When
        List<MenuGroupResponse> 조회된_메뉴그룹_목록 = menuGroupService.list();

        //Then
        verify(menuGroupRepository, times(1)).findAll();
        assertThat(조회된_메뉴그룹_목록).isNotNull()
                .hasSize(입력한_메뉴그룹_목록.size());
    }
}
