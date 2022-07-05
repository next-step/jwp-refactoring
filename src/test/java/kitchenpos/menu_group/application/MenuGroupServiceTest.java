package kitchenpos.menu_group.application;

import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.dto.MenuGroupRequest;
import kitchenpos.menu_group.dto.MenuGroupResponse;
import kitchenpos.menu_group.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 관련")
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    MenuGroupService menuGroupService;
    @MockBean
    MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 등록할 수 있다")
    @Test
    void create() {
        // given
        MenuGroupRequest given = new MenuGroupRequest("중식");
        when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(given.toEntity());

        // when
        MenuGroupResponse actual = menuGroupService.create(given);

        // then
        assertThat(actual.getName()).isEqualTo(given.getName());
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다")
    @Test
    void list() {
        // when
        menuGroupService.list();

        // then
        verify(menuGroupRepository).findAll();
    }
}
