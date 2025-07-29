package com.manguerasjc.productservice.services.DTO.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(@NotBlank(message = "El nombre de la categoría es obligatorio") String name) {
}
