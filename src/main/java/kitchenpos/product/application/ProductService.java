package kitchenpos.product.application;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(final ProductRequest productRequest) {
        Product persistProduct = productRepository.save(productRequest.toProduct());
        return ProductResponse.of(persistProduct);
    }

    public List<ProductResponse> list() {
        return findAll().stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public void checkExistsProducts(List<Long> productIds) {
        //List<Product> persistProducts = findAll();
        List<Product> persistProducts = productRepository.findAllById(productIds);
        if (productIds.size() != persistProducts.size()) {
            throw new NotFoundEntityException("등록되지 않은 상품이 있습니다.");
        }
//        products.forEach(product -> {
//            if (!persistProducts.contains(product)) {
//                throw new NotFoundEntityException("해당 Product를 찾을 수가 없습니다.");
//            }
//        });
    }

    public List<Product> findProductsByIds(List<Long> productIds) {
        return productRepository.findAllById(productIds);
    }

    private List<Product> findAll() {
        return productRepository.findAll();
    }
}
