package kitchenpos.application;

import static kitchenpos.domain.MenuGroupTestFixture.generateMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 햄버거세트;
    private MenuGroup 햄버거단품;

    @BeforeEach
    void setUp() {
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
        햄버거단품 = generateMenuGroup(2L, "햄버거단품");
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // given
        when(menuGroupDao.save(햄버거세트)).thenReturn(햄버거세트);

        // when
        MenuGroup saveMenuGroup = menuGroupService.create(햄버거세트);

        // then
        assertAll(
                () -> assertThat(saveMenuGroup.getId()).isNotNull(),
                () -> assertThat(saveMenuGroup.getName()).isEqualTo(햄버거세트.getName())
        );
    }

    @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
    @Test
    void findAllMenuGroups() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(햄버거세트, 햄버거단품);
        when(menuGroupDao.findAll()).thenReturn(menuGroups);

        // when
        List<MenuGroup> findMenuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(findMenuGroups).hasSize(menuGroups.size()),
                () -> assertThat(findMenuGroups).containsExactly(햄버거세트, 햄버거단품)
        );
    }
}
