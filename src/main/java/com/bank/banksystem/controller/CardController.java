package com.bank.banksystem.controller;

import com.bank.banksystem.dto.CardDTO;
import com.bank.banksystem.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardDTO> create(@Valid @RequestBody CardDTO dto) {
        return ResponseEntity.ok(cardService.createCard(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> get(@PathVariable UUID id) {
        return ResponseEntity.ok(cardService.getCard(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> update(@PathVariable UUID id, @Valid @RequestBody CardDTO dto) {
        return ResponseEntity.ok(cardService.updateCard(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CardDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(cardService.getAllCards(page, size));
    }
}
