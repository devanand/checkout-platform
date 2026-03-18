package com.haiilo.checkout.api.dto;

public record AppliedOfferResponse(
                String type,
                String description,
                boolean applied) {
}