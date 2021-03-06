package com.saasovation.identityaccess.domain.model.role;

import com.saasovation.common.domain.AbstractDomainEventSubscriber;
import com.saasovation.common.port.adapter.messaging.rabbitmq.ConnectionSettings;
import com.saasovation.common.port.adapter.messaging.rabbitmq.Exchange;
import com.saasovation.common.port.adapter.messaging.rabbitmq.MessageParameters;
import com.saasovation.common.port.adapter.messaging.rabbitmq.MessageProducer;
import com.saasovation.common.serializater.ObjectSerializer;
import com.saasovation.identityaccess.infrastructure.messaging.Exchanges;

import java.util.Date;
import java.util.UUID;

/**
 * @author : huanghy
 * @create : 2016/4/28 0028 ���� 10:22
 * @since : ${VERSION}
 */
public class UserAssignToRoleEventSubscriber extends AbstractDomainEventSubscriber<UserAssignToRole>{

    @Override
    public void handle(UserAssignToRole aDomainEvent) {
        String jsonString = ObjectSerializer.newInstance().serialize(aDomainEvent);
        System.out.println("===>"+jsonString);
        Exchange exchange = Exchange.directInstance(
                ConnectionSettings.instance(),
                Exchanges.IDENTITY_ACCESS_EXCHANGE_NAME,
                true);
        MessageProducer producer = new MessageProducer(exchange);
        MessageParameters parameters =  MessageParameters.durableTextParameters(
                "com.saasovation.identityacess.domain.model.role.UserAssignToRole",
                UUID.randomUUID().toString(),
                new Date(), "*");
        producer.send(jsonString,parameters)
                .close();
        System.out.println("===>send message to mq.");
    }
}
