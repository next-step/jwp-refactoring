package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    @DisplayName("상품은 이름과 가격을 통해 생성 할 수 있다.")
    @Test
    void create() {
        // given
        Product request = getCreateRequest("눈내리는치킨", 17_000);
        Product expected = getProduct(1L, "눈내리는치킨", 17_000);
        given(productDao.save(request)).willReturn(expected);
        // when
        Product actual = productService.create(request);
        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }


    @DisplayName("상품을 생성할 수 없는 경우")
    @Nested
    class CreateFailTest {

        @DisplayName("가격이 존재 하지 않는 경우 생성할 수 없다.")
        @Test
        void createByEmptyPrice() {
            // given
            Product request = getCreateRequest("쌀국수", null);
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> productService.create(request);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }


        @DisplayName("가격이 0 미만 일 경우 생성할 수 없다.")
        @Test
        void createByZeroMoreLessPrice() {
            // given
            Product request = getCreateRequest("쌀국수", -121);
            // when
            ThrowableAssert.ThrowingCallable createCall = () -> productService.create(request);
            // then
            assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Product 눈내리는치킨 = getProduct(1L, "눈내리는치킨", 17_000);
        final Product 쌀국수 = getProduct(2L, "쌀국수", 7_000);
        final List<Product> expected = Arrays.asList(눈내리는치킨, 쌀국수);
        given(productDao.findAll()).willReturn(expected);
        // when
        List<Product> list = productService.list();
        // then
        assertThat(list).containsExactlyElementsOf(expected);
    }

    public static Product getProduct(Long id, String name, int price) {
        final Product expected = new Product();
        expected.setId(id);
        expected.setName(name);
        expected.setPrice(BigDecimal.valueOf(price));
        return expected;
    }

    private Product getCreateRequest(String name, int price) {
        return getCreateRequest(name, BigDecimal.valueOf(price));
    }

    private Product getCreateRequest(String name, BigDecimal price) {
        final Product request = new Product();
        request.setName(name);
        request.setPrice(price);
        return request;
    }
}