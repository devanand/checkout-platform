package com.haiilo.checkout.api.controller;

import com.haiilo.checkout.api.dto.CheckoutItemRequest;
import com.haiilo.checkout.api.dto.CheckoutRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class CheckoutControllerIT {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    // -----------------------------------------------------------------------
    // Happy path — multi-buy offer applied
    // -----------------------------------------------------------------------

    @Test
    void appliesMultiBuyOffer() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
                List.of(new CheckoutItemRequest("APPLE", 2))
        );

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value("APPLE"))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].unitPrice").value("0.30"))
                .andExpect(jsonPath("$.items[0].lineTotal").value("0.45"))
                .andExpect(jsonPath("$.items[0].currency").value("EUR"))
                .andExpect(jsonPath("$.items[0].availableOffer.type").value("MULTI_BUY"))
                .andExpect(jsonPath("$.items[0].availableOffer.applied").value(true))
                .andExpect(jsonPath("$.total").value("0.45"))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andDo(document("checkout/multi-buy",
                        requestFields(
                                fieldWithPath("items").description("List of cart items"),
                                fieldWithPath("items[].productId").description("Product identifier"),
                                fieldWithPath("items[].quantity").description("Quantity requested")
                        ),
                        responseFields(
                                fieldWithPath("items").description("Priced line items"),
                                fieldWithPath("items[].productId").description("Product identifier"),
                                fieldWithPath("items[].quantity").description("Quantity"),
                                fieldWithPath("items[].unitPrice").description("Unit price before offer"),
                                fieldWithPath("items[].lineTotal").description("Final line total after offer"),
                                fieldWithPath("items[].currency").description("Currency code"),
                                fieldWithPath("items[].availableOffer").description("Offer details"),
                                fieldWithPath("items[].availableOffer.type").description("Offer type"),
                                fieldWithPath("items[].availableOffer.description").description("Human readable offer description"),
                                fieldWithPath("items[].availableOffer.applied").description("Whether the offer was applied"),
                                fieldWithPath("total").description("Overall cart total"),
                                fieldWithPath("currency").description("Currency code")
                        )
                ));
    }

    // -----------------------------------------------------------------------
    // Happy path — percent discount offer applied
    // -----------------------------------------------------------------------

    @Test
    void appliesPercentDiscountOffer() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
                List.of(new CheckoutItemRequest("BANANA", 3))
        );

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value("BANANA"))
                .andExpect(jsonPath("$.items[0].quantity").value(3))
                .andExpect(jsonPath("$.items[0].unitPrice").value("0.20"))
                .andExpect(jsonPath("$.items[0].lineTotal").value("0.54"))
                .andExpect(jsonPath("$.items[0].currency").value("EUR"))
                .andExpect(jsonPath("$.items[0].availableOffer.type").value("PERCENT_DISCOUNT"))
                .andExpect(jsonPath("$.items[0].availableOffer.applied").value(true))
                .andExpect(jsonPath("$.total").value("0.54"))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andDo(document("checkout/percent-discount",
                        requestFields(
                                fieldWithPath("items").description("List of cart items"),
                                fieldWithPath("items[].productId").description("Product identifier"),
                                fieldWithPath("items[].quantity").description("Quantity requested")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("items[].availableOffer.type").description("Offer type"),
                                fieldWithPath("items[].availableOffer.applied").description("Whether the offer was applied"),
                                fieldWithPath("total").description("Overall cart total")
                        )
                ));
    }

    // -----------------------------------------------------------------------
    // Offer exists but threshold not met — offer shown but not applied
    // -----------------------------------------------------------------------

    @Test
    void offerNotAppliedWhenQuantityBelowThreshold() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
                List.of(new CheckoutItemRequest("APPLE", 1))  // needs 2 for MULTI_BUY
        );

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value("APPLE"))
                .andExpect(jsonPath("$.items[0].lineTotal").value("0.30"))
                .andExpect(jsonPath("$.items[0].availableOffer.type").value("MULTI_BUY"))
                .andExpect(jsonPath("$.items[0].availableOffer.applied").value(false))
                .andExpect(jsonPath("$.total").value("0.30"))
                .andDo(document("checkout/offer-not-applied",
                        requestFields(
                                fieldWithPath("items").description("List of cart items"),
                                fieldWithPath("items[].productId").description("Product identifier"),
                                fieldWithPath("items[].quantity").description("Quantity requested")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("items[].availableOffer.type").description("Offer type — present but not applied"),
                                fieldWithPath("items[].availableOffer.applied").description("false — quantity below threshold"),
                                fieldWithPath("total").description("Regular price, no discount")
                        )
                ));
    }

    // -----------------------------------------------------------------------
    // Happy path — multiple items, mixed offers
    // -----------------------------------------------------------------------

    @Test
    void checkoutWithMultipleItems() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
                List.of(
                        new CheckoutItemRequest("APPLE", 2),
                        new CheckoutItemRequest("BANANA", 3)
                )
        );

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.items[0].lineTotal").value("0.45"))
                .andExpect(jsonPath("$.items[1].lineTotal").value("0.54"))
                .andExpect(jsonPath("$.total").value("0.99"))
                .andExpect(jsonPath("$.currency").value("EUR"))
                .andDo(document("checkout/multiple-items",
                        requestFields(
                                fieldWithPath("items").description("List of cart items"),
                                fieldWithPath("items[].productId").description("Product identifier"),
                                fieldWithPath("items[].quantity").description("Quantity requested")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("total").description("Sum of all line totals after offers"),
                                fieldWithPath("currency").description("Currency code")
                        )
                ));
    }

    // -----------------------------------------------------------------------
    // Error — unknown product
    // -----------------------------------------------------------------------

    @Test
    void returnsNotFoundForUnknownProduct() throws Exception {
        CheckoutRequest request = new CheckoutRequest(
                List.of(new CheckoutItemRequest("MANGO", 1))
        );

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.title").value("Unknown Product"))
                .andExpect(jsonPath("$.detail").value("Unknown product: MANGO"))
                .andExpect(jsonPath("$.errorCode").value("UNKNOWN_PRODUCT"))
                .andDo(document("checkout/unknown-product",
                        requestFields(
                                fieldWithPath("items").description("List of cart items"),
                                fieldWithPath("items[].productId").description("Unknown product identifier"),
                                fieldWithPath("items[].quantity").description("Quantity requested")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("status").description("HTTP status code"),
                                fieldWithPath("title").description("Error title"),
                                fieldWithPath("detail").description("Error detail message"),
                                fieldWithPath("errorCode").description("Machine-readable error code")
                        )
                ));
    }

    // -----------------------------------------------------------------------
    // Error — empty items list
    // -----------------------------------------------------------------------

    @Test
    void returnsBadRequestForEmptyItems() throws Exception {
        CheckoutRequest request = new CheckoutRequest(List.of());

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(document("checkout/empty-items",
                        requestFields(
                                fieldWithPath("items").description("Empty list — must contain at least one item")
                        )
                ));
    }

    // -----------------------------------------------------------------------
    // Error — invalid quantity
    // -----------------------------------------------------------------------

    @Test
    void returnsBadRequestForInvalidQuantity() throws Exception {
        String invalidRequest = """
                {
                  "items": [
                    { "productId": "APPLE", "quantity": 0 }
                  ]
                }
                """;

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andDo(document("checkout/invalid-quantity",
                        requestFields(
                                fieldWithPath("items").description("List of cart items"),
                                fieldWithPath("items[].productId").description("Product identifier"),
                                fieldWithPath("items[].quantity").description("Invalid quantity — must be at least 1")
                        )
                ));
    }

    // -----------------------------------------------------------------------
    // Error — blank productId
    // -----------------------------------------------------------------------

    @Test
    void returnsBadRequestForBlankProductId() throws Exception {
        String invalidRequest = """
                {
                  "items": [
                    { "productId": "", "quantity": 1 }
                  ]
                }
                """;

        mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/api/v1/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andDo(document("checkout/blank-product-id",
                        requestFields(
                                fieldWithPath("items").description("List of cart items"),
                                fieldWithPath("items[].productId").description("Blank product ID — must not be blank"),
                                fieldWithPath("items[].quantity").description("Quantity requested")
                        )
                ));
    }
}
