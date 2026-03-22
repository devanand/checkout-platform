package com.haiilo.checkout.pricing;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.api.pricing.PricingRequest;
import com.haiilo.checkout.api.pricing.PricingResponse;
import com.haiilo.checkout.pricing.catalog.ProductCatalog;
import com.haiilo.checkout.pricing.exception.UnknownProductException;
import com.haiilo.checkout.pricing.model.Product;
import com.haiilo.checkout.pricing.model.ProductId;
import com.haiilo.checkout.pricing.port.OfferPort;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PricingServiceTest {

    private final ProductCatalog catalog = mock(ProductCatalog.class);
    private final OfferPort offerPort = mock(OfferPort.class);
    private final PricingService pricingService = new PricingService(catalog, offerPort);

    private static final Money APPLE_PRICE = Money.of("0.30", "EUR");
    private static final Money BANANA_PRICE = Money.of("0.20", "EUR");

    @Test
    void pricesSingleItemWithNoOffer() {
        when(catalog.findById(ProductId.of("APPLE")))
                .thenReturn(Optional.of(new Product(ProductId.of("APPLE"), APPLE_PRICE)));
        when(offerPort.applyOffers(anyList()))
                .thenReturn(List.of(OfferResult.noOffer(Money.of("0.30", "EUR"))));

        List<PricingResponse> responses = pricingService.price(
                List.of(new PricingRequest("APPLE", 1, "EUR"))
        );

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).productId()).isEqualTo("APPLE");
        assertThat(responses.get(0).unitPrice()).isEqualTo(APPLE_PRICE);
        assertThat(responses.get(0).lineTotal().amount()).isEqualTo("0.30");
        assertThat(responses.get(0).offerApplied()).isFalse();
    }

    @Test
    void pricesSingleItemWithMultiBuyOffer() {
        when(catalog.findById(ProductId.of("APPLE")))
                .thenReturn(Optional.of(new Product(ProductId.of("APPLE"), APPLE_PRICE)));
        when(offerPort.applyOffers(anyList()))
                .thenReturn(List.of(new OfferResult(
                        Money.of("0.45", "EUR"), "MULTI_BUY", "Buy 2 for 0.45", true
                )));

        List<PricingResponse> responses = pricingService.price(
                List.of(new PricingRequest("APPLE", 2, "EUR"))
        );

        assertThat(responses.get(0).lineTotal().amount()).isEqualTo("0.45");
        assertThat(responses.get(0).offerApplied()).isTrue();
        assertThat(responses.get(0).offerType()).isEqualTo("MULTI_BUY");
    }

    @Test
    void pricesSingleItemWithPercentDiscountOffer() {
        when(catalog.findById(ProductId.of("BANANA")))
                .thenReturn(Optional.of(new Product(ProductId.of("BANANA"), BANANA_PRICE)));
        when(offerPort.applyOffers(anyList()))
                .thenReturn(List.of(new OfferResult(
                        Money.of("0.54", "EUR"), "PERCENT_DISCOUNT", "10% off", true
                )));

        List<PricingResponse> responses = pricingService.price(
                List.of(new PricingRequest("BANANA", 3, "EUR"))
        );

        assertThat(responses.get(0).lineTotal().amount()).isEqualTo("0.54");
        assertThat(responses.get(0).offerApplied()).isTrue();
        assertThat(responses.get(0).offerType()).isEqualTo("PERCENT_DISCOUNT");
    }

    @Test
    void throwsForUnknownProduct() {
        when(catalog.findById(ProductId.of("MANGO")))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> pricingService.price(
                List.of(new PricingRequest("MANGO", 1, "EUR"))
        )).isInstanceOf(UnknownProductException.class)
                .hasMessageContaining("MANGO");
    }

    @Test
    void processesMultipleItemsInOrder() {
        when(catalog.findById(ProductId.of("APPLE")))
                .thenReturn(Optional.of(new Product(ProductId.of("APPLE"), APPLE_PRICE)));
        when(catalog.findById(ProductId.of("BANANA")))
                .thenReturn(Optional.of(new Product(ProductId.of("BANANA"), BANANA_PRICE)));
        when(offerPort.applyOffers(anyList()))
                .thenReturn(List.of(
                        OfferResult.noOffer(Money.of("0.30", "EUR")),
                        OfferResult.noOffer(Money.of("0.60", "EUR"))
                ));

        List<PricingResponse> responses = pricingService.price(List.of(
                new PricingRequest("APPLE", 1, "EUR"),
                new PricingRequest("BANANA", 3, "EUR")
        ));

        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).productId()).isEqualTo("APPLE");
        assertThat(responses.get(1).productId()).isEqualTo("BANANA");
    }

    @Test
    void rejectsNullRequestList() {
        assertThatThrownBy(() -> pricingService.price(null))
                .isInstanceOf(NullPointerException.class);
    }
}
