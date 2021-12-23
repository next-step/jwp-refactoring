package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.dto.MenuGroupRequest;
import kitchenpos.menu.domain.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 그룹 서비스")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 베스트메뉴;
    private MenuGroup 세트메뉴;

    @BeforeEach
    void setUp() {
        베스트메뉴 = new MenuGroup("베스트메뉴");
        세트메뉴 = new MenuGroup("세트메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void create() {
        when(menuGroupRepository.save(any())).thenReturn(베스트메뉴);

        MenuGroupResponse response = menuGroupService.create(new MenuGroupRequest("베스트메뉴"));

        verify(menuGroupRepository, times(1)).save(any(MenuGroup.class));
        assertThat(response.getName()).isEqualTo(this.베스트메뉴.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void list() {
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(베스트메뉴, 세트메뉴));

        List<MenuGroupResponse> responses = menuGroupService.list();

        verify(menuGroupRepository, times(1)).findAll();
        assertThat(responses).hasSize(2);
    }
}