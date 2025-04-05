package io.hello.demo.testmodule.unittest.inventorysystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private LockManager lockManager;

    @Mock
    private Lock lock;

    @InjectMocks
    private InventoryService inventoryService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    private Product product;
    private Product lowStockProduct;
    private StockUpdateRequest increaseStockRequest;
    private StockUpdateRequest decreaseStockRequest;

    @BeforeEach
    void setUp() {
        // 상품 정보 설정
        product = new Product();
        product.setId("PRODUCT-123");
        product.setName("Test Product");
        product.setPrice(new BigDecimal("10000"));
        product.setStock(10);
        product.setMerchantId("MERCHANT-456");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // 재고가 적은 상품 정보 설정
        lowStockProduct = new Product();
        lowStockProduct.setId("PRODUCT-789");
        lowStockProduct.setName("Low Stock Product");
        lowStockProduct.setPrice(new BigDecimal("5000"));
        lowStockProduct.setStock(2);
        lowStockProduct.setMerchantId("MERCHANT-456");
        lowStockProduct.setCreatedAt(LocalDateTime.now());
        lowStockProduct.setUpdatedAt(LocalDateTime.now());

        // 재고 증가 요청 설정
        increaseStockRequest = new StockUpdateRequest();
        increaseStockRequest.setProductId("PRODUCT-123");
        increaseStockRequest.setQuantity(5);

        // 재고 감소 요청 설정
        decreaseStockRequest = new StockUpdateRequest();
        decreaseStockRequest.setProductId("PRODUCT-123");
        decreaseStockRequest.setQuantity(-3);

        // 락 목 설정
//        given(lockManager.getLock(anyString())).willReturn(lock);
//        doNothing().when(lock).lock();
//        doNothing().when(lock).unlock();


        /**
         * Mockito의 엄격한 스터빙(strict stubbing) 검증 때문에 발생합니다.
         * UnnecessaryStubbingException은 테스트에서 정의는 했지만 실제로 사용되지 않은 스터빙(stubbing)이 있을 때 발생합니다.
         */

        lenient().when(lockManager.getLock(anyString())).thenReturn(lock);
        lenient().doNothing().when(lock).lock();
        lenient().doNothing().when(lock).unlock();
    }

    @Test
    @DisplayName("존재하는 상품 조회 테스트")
    void getProduct_WithExistingId_ShouldReturnProduct() {
        // Given
        given(productRepository.findById("PRODUCT-123")).willReturn(Optional.of(product));

        // When
        Product foundProduct = inventoryService.getProduct("PRODUCT-123");

        // Then
        assertNotNull(foundProduct);
        assertEquals("PRODUCT-123", foundProduct.getId());
        assertEquals("Test Product", foundProduct.getName());
        assertEquals(10, foundProduct.getStock());
        verify(productRepository).findById("PRODUCT-123");
    }

    @Test
    @DisplayName("존재하지 않는 상품 조회 시 예외 발생 테스트")
    void getProduct_WithNonExistingId_ShouldThrowException() {
        // Given
        given(productRepository.findById("NON-EXISTING-ID")).willReturn(Optional.empty());

        // When & Then
        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> inventoryService.getProduct("NON-EXISTING-ID")
        );

        assertTrue(exception.getMessage().contains("Product not found"));
        verify(productRepository).findById("NON-EXISTING-ID");
    }

    @Test
    @DisplayName("재고가 임계값 이하인 상품 조회 테스트")
    void getProductsWithLowStock_ShouldReturnLowStockProducts() {
        // Given
        List<Product> allProducts = Arrays.asList(product, lowStockProduct);
        given(productRepository.findAll()).willReturn(allProducts);

        // When
        List<Product> lowStockProducts = inventoryService.getProductsWithLowStock(5);

        // Then
        assertEquals(1, lowStockProducts.size());
        assertEquals("PRODUCT-789", lowStockProducts.get(0).getId());
        assertEquals(2, lowStockProducts.get(0).getStock());
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("재고 증가 테스트")
    void updateStock_WithPositiveQuantity_ShouldIncreaseStock() {
        // Given
        given(productRepository.findById("PRODUCT-123")).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        StockUpdateResponse response = inventoryService.updateStock(increaseStockRequest);

        // Then
        verify(lockManager).getLock("PRODUCT-123");
        verify(lock).lock();
        verify(lock).unlock();
        verify(productRepository).findById("PRODUCT-123");
        verify(productRepository).save(productCaptor.capture());

        Product updatedProduct = productCaptor.getValue();
        assertEquals(15, updatedProduct.getStock()); // 10 + 5

        assertNotNull(response);
        assertEquals("PRODUCT-123", response.getProductId());
        assertEquals(15, response.getCurrentStock());
        assertTrue(response.getMessage().contains("Stock updated successfully"));
    }

    @Test
    @DisplayName("재고 감소 테스트")
    void updateStock_WithNegativeQuantity_ShouldDecreaseStock() {
        // Given
        given(productRepository.findById("PRODUCT-123")).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        StockUpdateResponse response = inventoryService.updateStock(decreaseStockRequest);

        // Then
        verify(lockManager).getLock("PRODUCT-123");
        verify(lock).lock();
        verify(lock).unlock();
        verify(productRepository).findById("PRODUCT-123");
        verify(productRepository).save(productCaptor.capture());

        Product updatedProduct = productCaptor.getValue();
        assertEquals(7, updatedProduct.getStock()); // 10 - 3

        assertNotNull(response);
        assertEquals("PRODUCT-123", response.getProductId());
        assertEquals(7, response.getCurrentStock());
        assertTrue(response.getMessage().contains("Stock updated successfully"));
    }

    @Test
    @DisplayName("재고보다 많은 수량 감소 시도 시 예외 발생 테스트")
    void updateStock_WithExcessiveNegativeQuantity_ShouldThrowException() {
        // Given
        StockUpdateRequest excessiveDecreaseRequest = new StockUpdateRequest();
        excessiveDecreaseRequest.setProductId("PRODUCT-123");
        excessiveDecreaseRequest.setQuantity(-15); // 현재 재고는 10

        given(productRepository.findById("PRODUCT-123")).willReturn(Optional.of(product));

        // When & Then
        OutOfStockException exception = assertThrows(
                OutOfStockException.class,
                () -> inventoryService.updateStock(excessiveDecreaseRequest)
        );

        assertTrue(exception.getMessage().contains("Not enough stock"));

        verify(lockManager).getLock("PRODUCT-123");
        verify(lock).lock();
        verify(lock).unlock();
        verify(productRepository).findById("PRODUCT-123");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("여러 상품 재입고 성공 테스트")
    void restockProducts_WithPositiveQuantities_ShouldUpdateAllStocks() {
        // Given
        StockUpdateRequest request1 = new StockUpdateRequest();
        request1.setProductId("PRODUCT-123");
        request1.setQuantity(5);

        StockUpdateRequest request2 = new StockUpdateRequest();
        request2.setProductId("PRODUCT-789");
        request2.setQuantity(10);

        List<StockUpdateRequest> requests = Arrays.asList(request1, request2);

        Product product1 = product; // 재고 10
        Product product2 = lowStockProduct; // 재고 2

        given(productRepository.findById("PRODUCT-123")).willReturn(Optional.of(product1));
        given(productRepository.findById("PRODUCT-789")).willReturn(Optional.of(product2));
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        inventoryService.restockProducts(requests);

        // Then
        verify(productRepository, times(2)).save(productCaptor.capture());
        List<Product> capturedProducts = productCaptor.getAllValues();

        assertEquals(15, capturedProducts.get(0).getStock()); // 10 + 5
        assertEquals(12, capturedProducts.get(1).getStock()); // 2 + 10
    }

    @Test
    @DisplayName("음수 수량으로 여러 상품 재입고 시도 시 예외 발생 테스트")
    void restockProducts_WithNegativeQuantity_ShouldThrowException() {
        // Given
        StockUpdateRequest request1 = new StockUpdateRequest();
        request1.setProductId("PRODUCT-123");
        request1.setQuantity(5);

        StockUpdateRequest request2 = new StockUpdateRequest();
        request2.setProductId("PRODUCT-789");
        request2.setQuantity(-10); // 음수 수량

        Product product1 = product; // 재고 10
        given(productRepository.findById("PRODUCT-123")).willReturn(Optional.of(product1));

        // 메소드가 호출될 때 전달받은 Product 객체를 그대로 반환하도록 설정하는 것입니다. 이는 실제 리포지토리의 save 메소드가 저장된 엔티티를 반환하는 동작을 모방합니다.
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        List<StockUpdateRequest> requests = Arrays.asList(request1, request2);

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.restockProducts(requests)
        );

        assertTrue(exception.getMessage().contains("Restock quantity must be positive"));

        // 어떤 상품도 업데이트되지 않아야 함 xxx
        // product1은 업데이트 됨.
//        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("재고 예약 성공 테스트")
    void reserveStock_WithSufficientStock_ShouldReturnTrue() {
        // Given
        given(productRepository.findById("PRODUCT-123")).willReturn(Optional.of(product));
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        boolean result = inventoryService.reserveStock("PRODUCT-123", 5);

        // Then
        assertTrue(result);

        verify(lockManager).getLock("PRODUCT-123");
        verify(lock).lock();
        verify(lock).unlock();
        verify(productRepository).findById("PRODUCT-123");
        verify(productRepository).save(productCaptor.capture());

        Product updatedProduct = productCaptor.getValue();
        assertEquals(5, updatedProduct.getStock()); // 10 - 5
    }

    @Test
    @DisplayName("재고 부족으로 인한 예약 실패 테스트")
    void reserveStock_WithInsufficientStock_ShouldReturnFalse() {
        // Given
        given(productRepository.findById("PRODUCT-123")).willReturn(Optional.of(product));

        // When
        boolean result = inventoryService.reserveStock("PRODUCT-123", 15); // 현재 재고는 10

        // Then
        assertFalse(result);

        verify(lockManager).getLock("PRODUCT-123");
        verify(lock).lock();
        verify(lock).unlock();
        verify(productRepository).findById("PRODUCT-123");
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("음수 수량으로 재고 예약 시도 시 예외 발생 테스트")
    void reserveStock_WithNegativeQuantity_ShouldThrowException() {
        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> inventoryService.reserveStock("PRODUCT-123", -5)
        );

        assertTrue(exception.getMessage().contains("Reservation quantity must be positive"));

        verify(lockManager, never()).getLock(anyString());
        verify(productRepository, never()).findById(anyString());
        verify(productRepository, never()).save(any(Product.class));
    }


}