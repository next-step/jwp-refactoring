package kitchenpos.application.menu;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성 테스트")
    @Test
    void createTest() {
        // given
        MenuGroupRequest expected = new MenuGroupRequest("메뉴그룹1");
        Mockito.when(menuGroupRepository.save(any())).thenReturn(expected.toMenuGroup());

        // when
        MenuGroupResponse actual = menuGroupService.create(expected);

        // then
        assertThat(actual).isNotNull()
                          .extracting(MenuGroupResponse::getName)
                          .isEqualTo(expected.getName());
    }

    @DisplayName("전체 메뉴 그룹 조회 테스트")
    @Test
    void listTest() {
        // given
        MenuGroup expected = new MenuGroup("메뉴그룹1");
        Mockito.when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(expected));

        // when
        List<MenuGroupResponse> actual = menuGroupService.list();

        // then
        assertThat(actual).isNotEmpty().hasSize(1);
    }

}
