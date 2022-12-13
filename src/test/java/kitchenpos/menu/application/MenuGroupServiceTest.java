package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroup.of(1L, "menuGroup");
    }

    @Test
    @DisplayName("메뉴 그룹 생성")
    public void createMenuGroup() {
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(menuGroup);

        MenuGroupResponse response = menuGroupService.create(MenuGroupRequest.of(menuGroup.getName()));
        assertThat(response.getName()).isEqualTo(menuGroup.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 조회")
    public void queryMenuGroup() {
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(menuGroup));

        List<MenuGroupResponse> response = menuGroupService.list();

        assertThat(response.size()).isEqualTo(1);
    }
}