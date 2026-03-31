package com.manguerasjc.productservice.services.DTO.request;

import com.manguerasjc.productservice.dataAccess.EGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequestDTO(@NotBlank(message = "El nombre de la categoría es obligatorio") String name, @NotNull(message = "El género es obligatorio(HOMBRE O MUJER)")
                                 EGender gender) {
}
