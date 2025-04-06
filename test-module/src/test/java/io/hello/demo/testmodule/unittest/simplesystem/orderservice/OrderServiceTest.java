package io.hello.demo.testmodule.unittest.simplesystem.orderservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {


    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockService stockService;

    @InjectMocks
    private OrderService orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Test
    void 주문_성공_테스트() {
        // given
        String productId = "P001";
        int quantity = 2;

        Order order = new Order(productId, quantity);
        when(stockService.hasStock(productId, quantity)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // when
        Order createdOrder = orderService.createOrder(productId, quantity);

        // then
        assertNotNull(createdOrder);
        assertEquals(productId, createdOrder.getProductId());
        assertEquals(quantity, createdOrder.getQuantity());
        verify(stockService, times(1)).hasStock(productId, quantity);
        verify(orderRepository, times(1)).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals(productId, savedOrder.getProductId());
        assertEquals(quantity, savedOrder.getQuantity());
    }

    @Test
    void 주문_실패_재고부족_테스트() {
        // given
        when(stockService.hasStock(anyString(), anyInt())).thenReturn(false);

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.createOrder("P001", 2);
        });
        assertEquals("재고 부족", exception.getMessage());
    }

    @Test
    void 주문_취소_테스트() {
        // given
        Long orderId = 1L;

        // when
        orderService.cancelOrder(orderId);

        verify(orderRepository, times(1)).deleteById(orderId);
    }
}