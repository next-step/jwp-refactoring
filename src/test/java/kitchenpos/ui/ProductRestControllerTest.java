package kitchenpos.ui;

import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
class ProductRestControllerTest {

    private Product 불고기버거;
    private Product 치킨버거;

    @Autowired
    private ProductRestController controller;

    @BeforeEach
    void setUp() {
        불고기버거 = new Product();
        불고기버거.setName("불고기버거");
        불고기버거.setPrice(BigDecimal.valueOf(4000));

        치킨버거 = new Product();
        치킨버거.setName("치킨버거");
        치킨버거.setPrice(BigDecimal.valueOf(3500));
    }

    @DisplayName("상품명과 가격 정보를 입력해 상품을 등록한다")
    @Test
    void createProduct() {
        // given when
        ResponseEntity<Product> productResponse = controller.create(불고기버거);

        // then
        Product actual = productResponse.getBody();
        assertThat(actual).isNotNull();
        assertThat(productResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(productResponse.getHeaders().getLocation().toString()).isEqualTo("/api/products/" + actual.getId());
        assertThat(actual.getName()).isEqualTo(불고기버거.getName());
        assertThat(actual.getPrice()).isEqualByComparingTo(불고기버거.getPrice());
    }

    @DisplayName("상품 등록 - 가격은 0 이상의 숫자를 입력해야 한다")
    @Test
    void createProduct_illegalMinPrice() {
        // given
        불고기버거.setPrice(BigDecimal.valueOf(-1));

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> controller.create(불고기버거));
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void findAll() {
        // given
        controller.create(불고기버거);
        controller.create(치킨버거);

        // when
        ResponseEntity<List<Product>> responseEntity = controller.list();
        List<Product> products = responseEntity.getBody();
        List<String> actual = products.stream()
                .map(Product::getName)
                .collect(Collectors.toList());

        // then
        assertThat(actual).containsAll(Arrays.asList("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨", "불고기버거", "치킨버거"));
    }
}