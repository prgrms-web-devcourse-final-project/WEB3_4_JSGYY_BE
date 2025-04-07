package com.ll.nbe344team7.global.config.webSocket.handler;

import com.ll.nbe344team7.global.config.webSocket.handler.command.ConnectHandler;
import com.ll.nbe344team7.global.config.webSocket.handler.command.DisConnectHandler;
import com.ll.nbe344team7.global.config.webSocket.handler.command.SubscribeHandler;
import com.ll.nbe344team7.global.config.webSocket.handler.command.UnSubscribeHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/**
 * 웹 소캣 명령
 *
 * @author jmkim
 * @since 25. 4. 7.
 */
@Component
public class StompHandler implements ChannelInterceptor {

    private final ConnectHandler connectHandler;
    private final SubscribeHandler subscribeHandler;
    private final UnSubscribeHandler unSubscribeHandler;
    private final DisConnectHandler disConnectHandler;

    public StompHandler(ConnectHandler connectHandler, SubscribeHandler subscribeHandler, UnSubscribeHandler unSubscribeHandler, DisConnectHandler disConnectHandler) {
        this.unSubscribeHandler = unSubscribeHandler;
        this.subscribeHandler = subscribeHandler;
        this.connectHandler = connectHandler;
        this.disConnectHandler = disConnectHandler;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        switch (accessor.getCommand()) {
            case CONNECT ->  connectHandler.handle(accessor);
            case SUBSCRIBE -> subscribeHandler.handle(accessor);
            case UNSUBSCRIBE -> unSubscribeHandler.handle(accessor);
            case DISCONNECT -> disConnectHandler.handle(accessor);
        }
        return message;
    }
}
