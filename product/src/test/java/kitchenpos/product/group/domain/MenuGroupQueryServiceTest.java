package kitchenpos.product.group.domain;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 쿼리 서비스")
@ExtendWith(MockitoExtension.class)
class MenuGroupQueryServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupQueryService queryService;

    @Test
    @DisplayName("메뉴 그룹 데이터 가져오기")
    void menuGroup() {
        //given
        long menuGroupId = 1L;

        //when
        queryService.menuGroup(menuGroupId);

        //then
        verify(menuGroupRepository, only()).menuGroup(menuGroupId);
    }

    @Test
    @DisplayName("메뉴 그룹들 데이터 가져오기")
    void findAll() {
        //when
        queryService.findAll();

        //then
        verify(menuGroupRepository, only()).findAll();
    }
}
