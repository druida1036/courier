package com.skipthedishes.courier.listeners;

import java.math.BigDecimal;
import java.time.Instant;

import com.skipthedishes.courier.entities.Delivery;
import com.skipthedishes.courier.entities.DeliveryAdjustment;
import com.skipthedishes.courier.entities.DeliveryBonus;
import com.skipthedishes.courier.mappers.DeliveryAdjustmentMapper;
import com.skipthedishes.courier.mappers.DeliveryBonusMapper;
import com.skipthedishes.courier.mappers.DeliveryMapper;
import com.skipthedishes.courier.models.AdjustmentModifiedEvent;
import com.skipthedishes.courier.models.BonusModifiedEvent;
import com.skipthedishes.courier.models.DeliveryCreatedEvent;
import com.skipthedishes.courier.repositories.DeliveryAdjustmentRepository;
import com.skipthedishes.courier.repositories.DeliveryBonusRepository;
import com.skipthedishes.courier.repositories.DeliveryRepository;
import com.skipthedishes.courier.services.DeliveryAdjustmentService;
import com.skipthedishes.courier.services.DeliveryBonusService;
import com.skipthedishes.courier.services.DeliveryService;
import com.skipthedishes.courier.utils.DateUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.support.MessageBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


@SpringBootTest
class DeliveryEventListenerTest {

    @Autowired
    private DeliveryChannels channels;

    @SpyBean
    private DeliveryService deliveryService;
    @SpyBean
    private DeliveryAdjustmentService deliveryAdjustmentService;
    @SpyBean
    private DeliveryBonusService deliveryBonusService;
    @SpyBean
    private DeliveryEventListener deliveryEventListener;
    @SpyBean
    private DeliveryRepository deliveryRepository;
    @SpyBean
    private DeliveryAdjustmentRepository deliveryAdjustmentRepository;
    @SpyBean
    private DeliveryBonusRepository deliveryBonusRepository;
    @SpyBean
    private DeliveryMapper deliveryMapper;
    @SpyBean
    private DeliveryAdjustmentMapper deliveryAdjustmentMapper;
    @SpyBean
    private DeliveryBonusMapper deliveryBonusMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(deliveryEventListener);
        verifyNoMoreInteractions(deliveryService);
        verifyNoMoreInteractions(deliveryRepository);
        verifyNoMoreInteractions(deliveryMapper);
        verifyNoMoreInteractions(deliveryAdjustmentService);
        verifyNoMoreInteractions(deliveryAdjustmentRepository);
        verifyNoMoreInteractions(deliveryAdjustmentMapper);
        verifyNoMoreInteractions(deliveryBonusService);
        verifyNoMoreInteractions(deliveryBonusRepository);
        verifyNoMoreInteractions(deliveryBonusMapper);
    }

    @Test
    void testDeliveryCreatedEventHandlerShouldProcess() {
        Instant now = Instant.now();
        DeliveryCreatedEvent event = DeliveryCreatedEvent.builder()
            .deliveryId(1)
            .courierId(1)
            .createdTimestamp(now.toEpochMilli())
            .value(BigDecimal.ONE)
            .build();
        channels.deliveryCreated().send(MessageBuilder.withPayload(event).build());
        verify(deliveryEventListener).deliveryCreatedEventHandler(event);
        verify(deliveryService).save(event);
        verify(deliveryRepository).findByExternalDeliveryId(event.getDeliveryId());
        verify(deliveryMapper).update(any(), any(), any());
        ArgumentCaptor<Delivery> deliveryArgumentCaptor = ArgumentCaptor.forClass(Delivery.class);
        verify(deliveryRepository).save(deliveryArgumentCaptor.capture());

        assertAll(
            () -> assertEquals(event.getCourierId(), deliveryArgumentCaptor.getValue().getCourierId()),
            () -> assertEquals(event.getDeliveryId(), deliveryArgumentCaptor.getValue().getExternalDeliveryId()),
            () -> assertEquals(DateUtils.toDateTime(now.toEpochMilli()),
                deliveryArgumentCaptor.getValue().getCreatedAt()),
            () -> assertEquals(event.getValue(), deliveryArgumentCaptor.getValue().getAmount())
        );
    }

    @Test
    void testDeliveryCreatedEventHandlerWithInvalidMessageShouldThrow() {
        DeliveryCreatedEvent event = DeliveryCreatedEvent.builder().build();

        Exception exception = assertThrows(MethodArgumentNotValidException.class,
            () -> channels.deliveryCreated().send(MessageBuilder.withPayload(event).build()));

        assertThat(exception.getMessage(), containsString(
            "Field error in object 'Arg 0' on field 'createdTimestamp': rejected value [null]"));
    }

    @Test
    void testAdjustmentModifiedEventHandlerShouldProcess() {
        Instant now = Instant.now();
        AdjustmentModifiedEvent event = AdjustmentModifiedEvent.builder()
            .adjustmentId(1)
            .deliveryId(1)
            .modifiedTimestamp(now.toEpochMilli())
            .value(BigDecimal.ONE)
            .build();
        channels.adjustmentModified().send(MessageBuilder.withPayload(event).build());
        verify(deliveryEventListener).adjustmentModifiedEventHandler(event);
        verify(deliveryAdjustmentService).save(event);
        verify(deliveryService).findByExternalDeliveryId(event.getDeliveryId());
        verify(deliveryRepository).findByExternalDeliveryId(event.getDeliveryId());
        verify(deliveryAdjustmentRepository).findByDeliveryIdAndExternalAdjustmentId(1, event.getAdjustmentId());
        verify(deliveryAdjustmentMapper).update(any(), any(), any());

        ArgumentCaptor<DeliveryAdjustment> deliveryArgumentCaptor = ArgumentCaptor.forClass(DeliveryAdjustment.class);
        verify(deliveryAdjustmentRepository).save(deliveryArgumentCaptor.capture());

        assertAll(
            () -> assertEquals(event.getAdjustmentId(), deliveryArgumentCaptor.getValue().getExternalAdjustmentId()),
            () -> assertEquals(event.getDeliveryId(),
                deliveryArgumentCaptor.getValue().getDelivery().getExternalDeliveryId()),
            () -> assertEquals(DateUtils.toDateTime(now.toEpochMilli()),
                deliveryArgumentCaptor.getValue().getModifiedAt()),
            () -> assertEquals(event.getValue(), deliveryArgumentCaptor.getValue().getAmount())
        );
    }

    @Test
    void testAdjustmentModifiedEventHandlerWithInvalidMessageShouldThrow() {
        AdjustmentModifiedEvent event = AdjustmentModifiedEvent.builder().build();
        Exception exception = assertThrows(MethodArgumentNotValidException.class,
            () -> channels.adjustmentModified().send(MessageBuilder.withPayload(event).build()));

        assertThat(exception.getMessage(), containsString(
            "Field error in object 'Arg 0' on field 'modifiedTimestamp': rejected value [null]"));
    }

    @Test
    void testBonusModifiedEventHandlerShouldProcess() {
        Instant now = Instant.now();
        BonusModifiedEvent event = BonusModifiedEvent.builder()
            .bonusId(1)
            .deliveryId(1)
            .modifiedTimestamp(now.toEpochMilli())
            .value(BigDecimal.ONE)
            .build();
        channels.bonusModified().send(MessageBuilder.withPayload(event).build());
        verify(deliveryEventListener).bonusModifiedEventHandler(event);
        verify(deliveryBonusService).save(event);
        verify(deliveryService).findByExternalDeliveryId(event.getDeliveryId());
        verify(deliveryRepository).findByExternalDeliveryId(event.getDeliveryId());
        verify(deliveryBonusRepository).findByDeliveryIdAndExternalBonusId(1, event.getBonusId());
        verify(deliveryBonusMapper).update(any(), any(), any());

        ArgumentCaptor<DeliveryBonus> deliveryArgumentCaptor = ArgumentCaptor.forClass(DeliveryBonus.class);
        verify(deliveryBonusRepository).save(deliveryArgumentCaptor.capture());

        assertAll(
            () -> assertEquals(event.getBonusId(), deliveryArgumentCaptor.getValue().getExternalBonusId()),
            () -> assertEquals(event.getDeliveryId(),
                deliveryArgumentCaptor.getValue().getDelivery().getExternalDeliveryId()),
            () -> assertEquals(DateUtils.toDateTime(now.toEpochMilli()),
                deliveryArgumentCaptor.getValue().getModifiedAt()),
            () -> assertEquals(event.getValue(), deliveryArgumentCaptor.getValue().getAmount())
        );
    }

    @Test
    void testBonusModifiedEventHandlerWithInvalidMessageShouldThrow() {
        BonusModifiedEvent event = BonusModifiedEvent.builder().build();

        Exception exception = assertThrows(MethodArgumentNotValidException.class,
            () -> channels.bonusModified().send(MessageBuilder.withPayload(event).build()));

        assertThat(exception.getMessage(), containsString(
            "Field error in object 'Arg 0' on field 'modifiedTimestamp': rejected value [null]"));
    }

}