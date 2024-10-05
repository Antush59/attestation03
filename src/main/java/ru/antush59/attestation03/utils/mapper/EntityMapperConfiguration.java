package ru.antush59.attestation03.utils.mapper;

import dev.akkinoc.spring.boot.orika.OrikaMapperFactoryConfigurer;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import org.springframework.context.annotation.Configuration;
import ru.antush59.attestation03.dao.entity.CustomerEntity;
import ru.antush59.attestation03.dao.entity.OptionEntity;
import ru.antush59.attestation03.dao.entity.OrderEntity;
import ru.antush59.attestation03.service.model.Customer;
import ru.antush59.attestation03.service.model.Option;
import ru.antush59.attestation03.service.model.Order;

@Configuration
public class EntityMapperConfiguration implements OrikaMapperFactoryConfigurer {

  @Override
  public void configure(MapperFactory mapperFactory) {
    mapperFactory.classMap(Option.class, OptionEntity.class)
        .byDefault()
        .register();

      mapperFactory.classMap(Customer.class, CustomerEntity.class)
              .byDefault()
              .register();

      mapperFactory.classMap(Order.class, OrderEntity.class)
              .byDefault()
              .register();

      mapperFactory.classMap(OrderEntity.class, Order.class)
              .customize(new CustomMapper<>() {
                  @Override
                  public void mapAtoB(OrderEntity entity,
                                      Order model,
                                      MappingContext context) {
                      model.setCustomerLogin(entity.getCustomer().getLogin());
                  }
              })
              .byDefault()
              .register();
  }
}
