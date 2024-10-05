package ru.antush59.attestation03.controller;

import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.MapperFacade;
import org.springframework.web.bind.annotation.*;
import ru.antush59.attestation03.controller.dto.request.OptionRequestDto;
import ru.antush59.attestation03.controller.dto.response.OptionResponseDto;
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
    public List<OptionResponseDto> getServices() {
        return optionsService.findAll().stream()
                .map(model -> mapper.map(model, OptionResponseDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public OptionResponseDto saveOption(@RequestBody OptionRequestDto optionDto) {
        Option option = mapper.map(optionDto, Option.class);
        Option saved = optionsService.save(option);
        return mapper.map(saved, OptionResponseDto.class);
    }

}

