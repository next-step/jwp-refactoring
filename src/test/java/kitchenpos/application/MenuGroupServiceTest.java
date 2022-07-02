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

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_요청데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        // given
        String name = "튀김류";
        MenuGroup request = 메뉴_그룹_요청데이터_생성(name);
        MenuGroup expected = 메뉴_그룹_생성(request.getName());

        given(menuGroupDao.save(any())).willReturn(expected);

        // when
        MenuGroup actual = menuGroupService.create(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        // given
        String name = "튀김류";
        MenuGroup request = 메뉴_그룹_요청데이터_생성(name);
        given(menuGroupDao.findAll()).willReturn(Collections.singletonList(메뉴_그룹_생성(request.getName())));

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(actual).isNotEmpty(),
                () -> assertThat(actual).hasSize(1)
        );
    }
}