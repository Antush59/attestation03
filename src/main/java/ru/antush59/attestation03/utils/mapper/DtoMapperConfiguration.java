package ru.antush59.attestation03.utils.mapper;

import dev.akkinoc.spring.boot.orika.OrikaMapperFactoryConfigurer;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import org.springframework.context.annotation.Configuration;
import ru.antush59.attestation03.controller.dto.request.CustomerRequestDto;
import ru.antush59.attestation03.controller.dto.request.OptionRequestDto;
import ru.antush59.attestation03.controller.dto.request.OrderRequestDto;
import ru.antush59.attestation03.controller.dto.response.CustomerResponseDto;
import ru.antush59.attestation03.controller.dto.response.OptionResponseDto;
import ru.antush59.attestation03.controller.dto.response.OrderResponseDto;
import ru.antush59.attestation03.service.model.Customer;
import ru.antush59.attestation03.service.model.Option;
import ru.antush59.attestation03.service.model.Order;

import java.util.stream.Collectors;

@Configuration
public class DtoMapperConfiguration implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory orikaMapperFactory) {
        orikaMapperFactory.classMap(OptionRequestDto.class, Option.class)
                .byDefault()
                .register();

        orikaMapperFactory.classMap(Option.class, OptionResponseDto.class)
                .byDefault()
                .register();

        orikaMapperFactory.classMap(CustomerRequestDto.class, Customer.class)
                .byDefault()
                .register();

        orikaMapperFactory.classMap(Customer.class, CustomerResponseDto.class)
                .byDefault()
                .register();

        orikaMapperFactory.classMap(OrderRequestDto.class, Order.class)
                .customize(new CustomMapper<>() {
                    @Override
                    public void mapAtoB(OrderRequestDto requestDto,
                                        Order model,
                                        MappingContext context) {
                        model.setOptions(requestDto.getOptions().stream()
                                .map(name -> new Option(null, name, null))
                                .collect(Collectors.toList()));
                    }
                })
                .byDefault()
                .register();

        orikaMapperFactory.classMap(Order.class, OrderResponseDto.class)
                .byDefault()
                .register();
    }
}
