package kitchenpos.product.domain;

import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.exception.IllegalPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.product.fixtures.ProductFixtures.양념치킨요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : kitchenpos.domain
 * fileName : ProductRepositoryTest
 * author : haedoang
 * date : 2021/12/21
 * description :
 */
@DataJpaTest
@DisplayName("상품 리파지토리 테스트")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        //when
        final List<Product> products = productRepository.findAll();

        //then
        assertThat(products.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    public void create() throws Exception {
        //given
        final ProductRequest 양념치킨 = 양념치킨요청(); //16000

        //when
        final Product actual = productRepository.save(양념치킨.toEntity());

        //then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("양념치킨"),
                () -> assertThat(actual.getPrice()).isEqualTo(new BigDecimal(16000))
        );
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다.")
    @ParameterizedTest(name = " 작은 수: " + ParameterizedTest.ARGUMENTS_PLACEHOLDER)
    @ValueSource(ints = {Integer.MIN_VALUE, -10, -5, -1})
    public void illegalPrice(int candidate) {
        //then
        assertThatThrownBy(() -> new Product("양념치킨", new BigDecimal(candidate)))
                .isInstanceOf(IllegalPriceException.class);
    }

    @Test
    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다. null")
    public void illegalPriceNull() throws Exception {
        //then
        assertThatThrownBy(() -> new Product("양념치킨", null))
                .isInstanceOf(IllegalPriceException.class);
    }
}
