package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Nested
    @DisplayName("create 메서드는")
    class Describe_create {

        @Nested
        @DisplayName("메뉴 그룹이 주어지면")
        class Context_with_menu_group {
            final MenuGroup expected = new MenuGroup();

            @BeforeEach
            void setUp() {
                when(menuGroupDao.save(any(MenuGroup.class)))
                        .thenReturn(expected);
            }

            @Test
            @DisplayName("주어진 메뉴 그룹을 저장하고, 저장된 객체를 리턴한다.")
            void it_returns_saved_menu_group() {
                MenuGroup actual = menuGroupService.create(expected);

                assertThat(actual).isEqualTo(expected);
            }
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class Describe_list {

        @Nested
        @DisplayName("저장된 메뉴 그룹이 주어지면")
        class Context_with_menu_groups {
            final MenuGroup menuGroup = new MenuGroup();
            final MenuGroup menuGroup2 = new MenuGroup();

            @BeforeEach
            void setUp() {
                when(menuGroupDao.findAll())
                        .thenReturn(Arrays.asList(menuGroup, menuGroup2));
            }

            @Test
            @DisplayName("메뉴 그룹 목록을 리턴한다.")
            void it_returns_menu_groups() {
                List<MenuGroup> actual = menuGroupService.list();
                assertThat(actual).containsExactly(menuGroup, menuGroup2);
            }
        }
    }
}
