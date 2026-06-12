package com.coursemanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Nazwa kategorii jest wymagana")
    @Size(min = 3, max = 50, message = "Nazwa kategorii musi mieć od 3 do 50 znaków")
    @Pattern(regexp = "^[A-Za-zĄĆĘŁŃÓŚŹŻąćęłńóśźż0-9 ._-]+$", message = "Nazwa zawiera niedozwolone znaki")
    private String name;
}
