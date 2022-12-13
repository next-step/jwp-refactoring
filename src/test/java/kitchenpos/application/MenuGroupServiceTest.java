package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("MenuGroupService 클래스 테스트")
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Nested
    @DisplayName("create 메서드 테스트")
    public class CreateMethod {
        @Test
        @DisplayName("메뉴그룹 생성 성공")
        public void success() {
            // given
            final MenuGroup mockMenuGroup = setupSuccess("test menu group");

            // when
            final MenuGroup createdMenuGroup = menuGroupService.create(mockMenuGroup);

            // then
            assertThat(createdMenuGroup.getName()).isEqualTo(mockMenuGroup.getName());
        }

        private MenuGroup setupSuccess(String name) {
            final MenuGroup mockMenuGroup = new MenuGroup();
            mockMenuGroup.setName("test menu group");
            Mockito.when(menuGroupDao.save(Mockito.any())).thenReturn(mockMenuGroup);
            return mockMenuGroup;
        }
    }

    @Nested
    @DisplayName("list 메서드 테스트")
    public class ListMethod {
        @Test
        @DisplayName("메뉴그룹 생성 성공")
        public void success() {
            // given
            final List<MenuGroup> mockMenuGroups = setupSuccess();

            // when
            final List<MenuGroup> createdMenuGroups = menuGroupService.list();

            // then
            assertThat(createdMenuGroups.size()).isEqualTo(mockMenuGroups.size());
        }

        private List<MenuGroup> setupSuccess() {
            final List<MenuGroup> mockMenuGroups = Arrays.asList(new MenuGroup(), new MenuGroup(), new MenuGroup());
            Mockito.when(menuGroupDao.findAll()).thenReturn(mockMenuGroups);
            return mockMenuGroups;
        }
    }
}
