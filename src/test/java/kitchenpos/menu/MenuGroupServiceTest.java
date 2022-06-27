package kitchenpos.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    MenuGroupService menuGroupService;

    @Mock
    MenuGroupDao menuGroupDao;

    MenuGroup 한마리메뉴 = new MenuGroup();

    @Test
    @DisplayName("메뉴 그룹을 저장한다")
    void create() {
        // given
        given(menuGroupDao.save(any())).willReturn(한마리메뉴);

        // when
        MenuGroup actual = menuGroupService.create(한마리메뉴);

        // then
        assertThat(actual).isEqualTo(한마리메뉴);
    }

    @Test
    @DisplayName("메뉴 그룹 리스트를 조회한다")
    void list() {
        // given
        given(menuGroupDao.findAll()).willReturn(Collections.singletonList(한마리메뉴));

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual).containsExactlyInAnyOrder(한마리메뉴)
        );
    }
}
