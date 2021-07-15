package kitchenpos.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    MenuGroup 한마리메뉴;
    MenuGroup 두마리메뉴;

    MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        한마리메뉴 = new MenuGroup(1L, "한마리메뉴");
        두마리메뉴 = new MenuGroup(2L, "두마리메뉴");

        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹을 등록")
    @Test
    void 메뉴그룹을_등록() {
        //given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("한마리메뉴");
        given(menuGroupRepository.save(any())).willReturn(한마리메뉴);

        //when
        MenuGroupResponse savedMenuGroupResponse = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(savedMenuGroupResponse.getName()).isEqualTo(한마리메뉴.getName());
        assertThat(savedMenuGroupResponse.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 목록을 불러옴")
    @Test
    void list() {
        //given
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(한마리메뉴, 두마리메뉴));

        //when
        List<MenuGroupResponse> list = menuGroupService.list();

        //then
        assertThat(list).hasSize(2);
        assertThat(list.stream().map(MenuGroupResponse::getName))
                .containsExactly(
                        한마리메뉴.getName(),
                        두마리메뉴.getName()
                );
    }
}
