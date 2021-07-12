package kitchenpos.product.application;

import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ProductService2 {

  private final ProductRepository productRepository;

  public ProductService2(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public ProductResponse create(final ProductRequest product) {
    return ProductResponse.from(productRepository.save(product.toEntity()));
  }

  @Transactional(readOnly = true)
  public List<ProductResponse> findAllProducts() {
    return ProductResponse.ofList(productRepository.findAll());
  }

}
