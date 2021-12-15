package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("메뉴 그룹 관리 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    public static final MenuGroup 패밀리_세트 = new MenuGroup("패밀리 세트");

    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹 생성")
    @Test
    void createTest() {
        // given
        when(menuGroupService.create(패밀리_세트))
                .thenReturn(패밀리_세트);

        // when
        MenuGroup actual = menuGroupService.create(패밀리_세트);

        // then
        assertThat(actual).isEqualTo(패밀리_세트);
    }

    @Test
    void list() {
        // given
        when(menuGroupService.list()).thenReturn(Collections.singletonList(패밀리_세트));

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactly(패밀리_세트);
    }
}