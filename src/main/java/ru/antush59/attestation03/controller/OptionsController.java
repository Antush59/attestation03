package ru.antush59.attestation03.controller;

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

@RestController
@RequestMapping("/options")
@RequiredArgsConstructor
public class OptionsController {

    private final OptionsService optionsService;
    private final MapperFacade mapper;

    @GetMapping
    public List<OptionResponseDto> getOptions(@RequestParam(required = false) String name) {
        if (name == null) {
            return getAllOptions();
        } else {
            return getByName(name);
        }
    }

    @PostMapping
    public OptionResponseDto saveOption(@RequestBody OptionRequestDto optionDto) {
        Option option = mapper.map(optionDto, Option.class);
        Option saved = optionsService.save(option);
        return mapper.map(saved, OptionResponseDto.class);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteByName(@RequestParam String name) {
        optionsService.deleteByName(name);
        return ResponseEntity.status(204).build();
    }

    @PutMapping
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

