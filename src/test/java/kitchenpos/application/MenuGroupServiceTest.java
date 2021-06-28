package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
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
        MenuGroup menuGroup = new MenuGroup();
        given(menuGroupDao.save(menuGroup))
                .willReturn(menuGroup);

        // when
        MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertThat(result)
                .isEqualTo(result);

        verify(menuGroupDao, VerificationModeFactory.only())
                .save(menuGroup);
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

        given(menuGroupDao.findAll())
                .willReturn(menuGroups);

        // when
        List<MenuGroup> list = menuGroupService.list();

        // then
        assertThat(list)
                .containsExactlyElementsOf(menuGroups);

        verify(menuGroupDao, VerificationModeFactory.only())
                .findAll();
    }
}