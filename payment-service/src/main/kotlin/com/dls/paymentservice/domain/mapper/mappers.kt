import com.dls.paymentservice.adapter.out.persistency.PaymentEntity
import com.dls.paymentservice.domain.event.PaymentProcessedEvent

fun PaymentProcessedEvent.toPaymentEntity() = PaymentEntity(paymentId, orderId)