package kitchenpos.application.menu;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService service;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    public void insertMenuGroupTest() {
        given(menuGroupRepository.save(any())).willReturn(new MenuGroup(1L,"그룹1"));

        MenuGroupResponse save = service.create(new MenuGroupRequest("그룹1"));

        assertThat(save.getId()).isEqualTo(1L);
        assertThat(save.getName()).isEqualTo("그룹1");
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    public void findAllMenuGroupTest() {
        MenuGroup menuGroup1 = new MenuGroup(1L,"그룹1");
        MenuGroup menuGroup2 = new MenuGroup(2L,"그룹2");
        given(menuGroupRepository.findAll()).willReturn(
                Arrays.asList(menuGroup1, menuGroup2)
        );

        List<MenuGroup> results = service.list();

        assertThat(results).contains(menuGroup1, menuGroup2);
    }
}