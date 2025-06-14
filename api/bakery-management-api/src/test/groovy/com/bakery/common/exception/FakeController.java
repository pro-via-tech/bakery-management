package com.bakery.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fake")
class FakeController {

    @GetMapping("/id-not-allowed")
    public void throwIdNotAllowed() {
        throw new IdNotAllowedException();
    }

    @GetMapping("/ingredient-not-found")
    public void throwIngredientNotFound() {
        throw new IngredientNotFoundException(10L);
    }

    @GetMapping("/recipe-not-found")
    public void throwRecipeNotFound() {
        throw new RecipeNotFoundException(12L);
    }

    @GetMapping("/data-integrity")
    public void throwDataIntegrity() {
        throw new DataIntegrityViolationException("Integrity issue");
    }

    @GetMapping("/generic")
    public void throwGeneric() {
        throw new RuntimeException("Something broke");
    }
}

