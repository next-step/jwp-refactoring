package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.group.MenuGroup;
import kitchenpos.domain.menu.group.MenuGroupCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        this.menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    @DisplayName("create - 정상적인 메뉴 그룹 저장")
    void 정상적인_메뉴_그룹_저장() {
        // given
        MenuGroupCreate menuGroupCreate = new MenuGroupCreate("Hello");

        // when
        when(menuGroupDao.save(any())).thenAnswer(i -> i.getArgument(0));

        MenuGroup result = menuGroupService.create(menuGroupCreate);

        // then
        assertThat(result.getName()).isEqualTo(menuGroupCreate.getName());

        verify(menuGroupDao, VerificationModeFactory.times(1))
                .save(result);
    }

    @Test
    @DisplayName("list - 정상적인 메뉴 그룹 조회")
    void 정상적인_메뉴_그룹_조회() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(
                new MenuGroup(1L, "A"),
                new MenuGroup(2L, "B"),
                new MenuGroup(3L, "C")
        );

        // when
        when(menuGroupDao.findAll())
                .thenReturn(menuGroups);

        List<MenuGroup> list = menuGroupService.list();

        // then
        assertThat(list)
                .containsExactlyElementsOf(menuGroups);

        verify(menuGroupDao, VerificationModeFactory.times(1))
                .findAll();
    }
}