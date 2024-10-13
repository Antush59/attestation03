package ru.antush59.attestation03.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.antush59.attestation03.controller.dto.request.OptionRequestDto;
import ru.antush59.attestation03.controller.dto.response.OptionResponseDto;
import ru.antush59.attestation03.controller.dto.update.OptionUpdateRequestDto;
import ru.antush59.attestation03.service.OptionsService;
import ru.antush59.attestation03.service.model.Option;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Услуги", description = "Контроллер CRUD операций для работы с услугами")
@RestController
@RequestMapping("/options")
@RequiredArgsConstructor
public class OptionsController {

    private final OptionsService optionsService;
    private final MapperFacade mapper;

    @Operation(summary = "Получить данные всех услуг или услуги по указанному названию")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный ответ"),
            @ApiResponse(responseCode = "500", description = "Задача не выполнена", content = @Content)
    })
    public List<OptionResponseDto> getOptions(@RequestParam(required = false) String name) {
        if (name == null) {
            return getAllOptions();
        } else {
            return getByName(name);
        }
    }

    @Operation(summary = "Сохранить данные новой услуги")
    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные сохранены"),
            @ApiResponse(responseCode = "500", description = "Данные не сохранены", content = @Content)
    })
    public OptionResponseDto saveOption(@RequestBody OptionRequestDto optionDto) {
        Option option = mapper.map(optionDto, Option.class);
        Option saved = optionsService.save(option);
        return mapper.map(saved, OptionResponseDto.class);
    }

    @Operation(summary = "Удалить данные услуги по указанному названию")
    @DeleteMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные удалены"),
            @ApiResponse(responseCode = "500", description = "Данные не удалены", content = @Content)
    })
    public ResponseEntity<Void> deleteByName(@RequestParam String name) {
        optionsService.deleteByName(name);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Изменить данные услуги")
    @PutMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные изменены"),
            @ApiResponse(responseCode = "500", description = "Данные не изменены", content = @Content)
    })
    public OptionResponseDto updateOption(@RequestBody OptionUpdateRequestDto optionUpdateDto) {
        Option option = mapper.map(optionUpdateDto, Option.class);
        Option saved = optionsService.save(option);
        return mapper.map(saved, OptionResponseDto.class);
    }

    private @NotNull List<OptionResponseDto> getByName(String name) {
        Option byName = optionsService.findByName(name);
        return List.of(mapper.map(byName, OptionResponseDto.class));
    }

    private @NotNull List<OptionResponseDto> getAllOptions() {
        return optionsService.findAll().stream()
                .map(model -> mapper.map(model, OptionResponseDto.class))
                .collect(Collectors.toList());
    }
}

