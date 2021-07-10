package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.ui.MenuGroupService;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 한마리메뉴;
    private MenuGroup 두마리메뉴;

    @BeforeEach
    void setUp() {
        한마리메뉴 = new MenuGroup("한마리메뉴");
        두마리메뉴 = new MenuGroup("두마리메뉴");
    }

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void 메뉴그룹_등록() {
        //Given
        when(menuGroupRepository.save(any())).thenReturn(한마리메뉴);

        //When
        MenuGroupResponse 생성된_메뉴그룹 = menuGroupService.create(MenuGroupRequest.of(한마리메뉴));

        //Then
        assertThat(생성된_메뉴그룹.getName()).isEqualTo(한마리메뉴.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void 메뉴그룹_목록_조회() {
        //Given
        List<MenuGroup> 입력한_메뉴그룹_목록 = new ArrayList<>(Arrays.asList(한마리메뉴, 두마리메뉴));
        when(menuGroupRepository.findAll()).thenReturn(입력한_메뉴그룹_목록);

        //When
        List<MenuGroupResponse> 조회된_메뉴그룹_목록 = menuGroupService.newList();

        //Then
        assertThat(조회된_메뉴그룹_목록).isNotNull()
                .hasSize(입력한_메뉴그룹_목록.size())
                .containsExactly(MenuGroupResponse.of(한마리메뉴), MenuGroupResponse.of(두마리메뉴));
    }
}
