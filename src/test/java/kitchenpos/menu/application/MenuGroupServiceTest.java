package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuGroupRequestDto;
import kitchenpos.menu.dto.MenuGroupResponseDto;
import kitchenpos.menu.repository.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.fixture.MenuGroupFixture.메뉴묶음_데이터_생성;
import static kitchenpos.common.fixture.MenuGroupFixture.메뉴묶음_요청데이터_생성;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 묶음을 생성한다.")
    @Test
    void create() {
        //given
        String name = "menuGroup";
        MenuGroupRequestDto request = 메뉴묶음_요청데이터_생성(name);

        Long id = 1L;
        given(menuGroupRepository.save(any())).willReturn(메뉴묶음_데이터_생성(id, name));

        //when
        MenuGroupResponseDto response = menuGroupService.create(request);

        //then
        메뉴묶음_확인(response, id, name);
    }

    @DisplayName("메뉴 묶음을 전체 조회한다.")
    @Test
    void list() {
        //given
        Long id = 1L;
        String name = "menuGroup";
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(메뉴묶음_데이터_생성(id, name)));

        //when
        List<MenuGroupResponseDto> responseList = menuGroupService.list();

        //then
        assertEquals(1, responseList.size());
        MenuGroupResponseDto response = responseList.get(0);
        메뉴묶음_확인(response, id, name);
    }

    private void 메뉴묶음_확인(MenuGroupResponseDto menuGroup, Long id, String name) {
        assertAll(
                () -> assertEquals(id, menuGroup.getId()),
                () -> assertEquals(name, menuGroup.getName())
        );
    }
}