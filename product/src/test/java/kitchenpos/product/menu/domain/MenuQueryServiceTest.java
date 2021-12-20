package kitchenpos.product.menu.domain;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 쿼리 서비스")
@ExtendWith(MockitoExtension.class)
class MenuQueryServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuQueryService queryService;

    @Test
    @DisplayName("메뉴들 데이터 가져오기")
    void findAll() {
        //when
        queryService.findAll();

        //then
        verify(menuRepository, only()).findAll();
    }

    @Test
    @DisplayName("id 목록으로 메뉴들 데이터 가져오기")
    void findAllById() {
        //given
        List<Long> ids = Arrays.asList(1L, 2L);

        //when
        queryService.findAllById(ids);

        //then
        verify(menuRepository, only()).findAllById(ids);
    }
}
