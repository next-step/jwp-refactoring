package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    private Product givenProduct = new Product();

    @BeforeEach
    void setup() {
        givenProduct.setId(1L);
        givenProduct.setPrice(BigDecimal.TEN);
        givenProduct.setName("샘플상품");
    }

/** 상품을 등록할 수 있다
  * 가격이 없는 상품은 등록할 수 없다
* 상품 목록을 조회할 수 있다*/


    @DisplayName("가격이 없거나 마이너스인 상품은 등록할 수 없다")
    @Test
    void createFailBecauseWrongPriceTest() {
        //given
        givenProduct.setPrice(null);

        //when && then
        assertThatThrownBy(() -> productService.create(givenProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 없는 상품은 등록할 수 없습니다");

        //given
        givenProduct.setPrice(BigDecimal.valueOf(-1));

        //when && then
        assertThatThrownBy(() -> productService.create(givenProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가격이 없는 상품은 등록할 수 없습니다");

    }

    @DisplayName("주문 생성")
    @Test
    void createTest() {
        //when
        productService.create(givenProduct);

        //then
        verify(productDao).save(givenProduct);
    }

    @DisplayName("주문 목록을 조회할 수 있다 ")
    @Test
    void list() {
        //given
        List<Product> expect = Arrays.asList(givenProduct);
        given(productDao.findAll())
                .willReturn(expect);

        //when
        List<Product> result = productService.list();

        //then
        verify(productDao).findAll();
        assertThat(result.size()).isEqualTo(expect.size());
        assertThat(result).containsExactly(givenProduct);
    }


}