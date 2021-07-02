package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 테스트")
class MenuGroupServiceTest {
    @Mock
    JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    MenuGroup menuGroup;

    @BeforeEach
    void setup() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("국밥");
    }

    @DisplayName("생성")
    @Test
    void create() {
        // given

        // when
        when(jdbcTemplateMenuGroupDao.save(menuGroup)).thenReturn(menuGroup);
        jdbcTemplateMenuGroupDao.save(menuGroup);
        // then
        assertThat(menuGroup).isNotNull();
        assertThat(menuGroup.getId()).isEqualTo(1L);
    }

    @DisplayName("조회")
    @Test
    void find() {
        // given

        // when
        when(jdbcTemplateMenuGroupDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(menuGroup)));
        List<MenuGroup> menuGroups = jdbcTemplateMenuGroupDao.findAll();

        // then
        assertThat(menuGroups.size()).isEqualTo(1);
        assertThat(menuGroups.get(0).getId()).isEqualTo(1L);
    }
}
