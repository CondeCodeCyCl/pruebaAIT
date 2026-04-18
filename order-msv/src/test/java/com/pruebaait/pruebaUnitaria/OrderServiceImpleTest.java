package com.pruebaait.pruebaUnitaria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.pruebaait.commons.clients.DriverClient;
import com.pruebaait.commons.dto.driver.DriverResponse;
import com.pruebaait.commons.dto.orders.OrderRequest;
import com.pruebaait.commons.dto.orders.OrderResponse;
import com.pruebaait.commons.enums.Status;
import com.pruebaait.entities.Order;
import com.pruebaait.mapper.OrderMapper;
import com.pruebaait.repositories.OrderRepository;
import com.pruebaait.services.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImpleTest {

	
	@Mock private OrderRepository orderRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private DriverClient driverClient;

    @InjectMocks private OrderServiceImpl orderService;

    @Test
    void createOrder_DebeRetornarOrdenCreada() {
        // Arrange
        OrderRequest request = new OrderRequest("Veracruz", "Puebla");
        Order orderEntity = new Order();
        OrderResponse responseEsperada = new OrderResponse(UUID.randomUUID(), Status.CREATED, "Veracruz", "Puebla", null, null, null);

        when(orderMapper.requestToEntity(request)).thenReturn(orderEntity);
        when(orderRepository.save(any(Order.class))).thenReturn(orderEntity);
        when(orderMapper.entityToResponse(orderEntity, null)).thenReturn(responseEsperada);

        // Act
        OrderResponse resultado = orderService.createOrder(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(Status.CREATED, resultado.status());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_AInTransit_DebeOcuparAlConductor() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(Status.CREATED);
        order.setIdDriver(driverId); // Ya tiene conductor asignado

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // Act: Cambiamos estado a IN_TRANSIT
        orderService.updateOrderStatus(orderId, Status.IN_TRANSIT);

        // Assert
        assertEquals(Status.IN_TRANSIT, order.getStatus());
        // 🚨 Verificamos que el sistema le avisó al otro microservicio que ocupara al conductor (false)
        verify(driverClient).updateDriverStatus(driverId, false);
    }

    @Test
    void updateOrderStatus_DeCanceladoAEntregado_DebeLanzarExcepcion() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order orderCancelada = new Order();
        orderCancelada.setId(orderId);
        orderCancelada.setStatus(Status.CANCELLED); // 🚨 Orden ya cancelada

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(orderCancelada));

        // Act & Assert
        IllegalStateException excepcion = assertThrows(IllegalStateException.class, () -> {
            orderService.updateOrderStatus(orderId, Status.DELIVERED); // Intentamos entregarla
        });

        assertTrue(excepcion.getMessage().contains("No se puede cambiar el estado de una orden finalizada"));
        verify(orderRepository, Mockito.never()).save(any(Order.class)); // Verificamos que se abortó el guardado
    }

    
    @Test
    void asignarConductor_ConDatosValidos_DebeGuardarArchivosYAsignar() throws IOException {
        // Arrange
        UUID orderId = UUID.randomUUID();
        UUID driverId = UUID.randomUUID();
        
        Order order = new Order();
        order.setStatus(Status.CREATED);
        
        DriverResponse driverActivo = new DriverResponse(driverId, "Carlos", "MX-123", true);

        // Mockeamos los archivos falsos
        MultipartFile pdfMock = mock(MultipartFile.class);
        when(pdfMock.getContentType()).thenReturn("application/pdf");
        when(pdfMock.isEmpty()).thenReturn(false);
        when(pdfMock.getBytes()).thenReturn(new byte[]{1, 2, 3}); // Simula bytes del PDF

        MultipartFile imgMock = mock(MultipartFile.class);
        when(imgMock.getContentType()).thenReturn("image/png");
        when(imgMock.isEmpty()).thenReturn(false);
        when(imgMock.getBytes()).thenReturn(new byte[]{4, 5, 6}); // Simula bytes de la imagen

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(driverClient.getDriverById(driverId)).thenReturn(driverActivo);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        orderService.asignarConductor(orderId, driverId, pdfMock, imgMock);

        assertEquals(driverId, order.getIdDriver());
        assertNotNull(order.getFilePdf());
        assertNotNull(order.getFileImage());
        verify(driverClient).updateDriverStatus(driverId, false);
        verify(orderRepository).save(order);
    }
}
