package com.kpodsiadlo.eightbitcomputer.handler;

import javax.websocket.CloseReason;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ForwardingSession implements Session {
    private final Session delegate;
    public ForwardingSession(Session delegate) {
        this.delegate = delegate;
    }

    @Override
    public WebSocketContainer getContainer() {
        return delegate.getContainer();
    }

    @Override
    public void addMessageHandler(MessageHandler handler) throws IllegalStateException {
        delegate.addMessageHandler(handler);
    }

    @Override
    public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Whole<T> handler) {
        delegate.addMessageHandler(clazz, handler);
    }

    @Override
    public <T> void addMessageHandler(Class<T> clazz, MessageHandler.Partial<T> handler) {
        delegate.addMessageHandler(clazz, handler);
    }

    @Override
    public Set<MessageHandler> getMessageHandlers() {
        return delegate.getMessageHandlers();
    }

    @Override
    public void removeMessageHandler(MessageHandler handler) {
        delegate.removeMessageHandler(handler);
    }

    @Override
    public String getProtocolVersion() {
        return delegate.getProtocolVersion();
    }

    @Override
    public String getNegotiatedSubprotocol() {
        return delegate.getNegotiatedSubprotocol();
    }

    @Override
    public List<Extension> getNegotiatedExtensions() {
        return delegate.getNegotiatedExtensions();
    }

    @Override
    public boolean isSecure() {
        return delegate.isSecure();
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public long getMaxIdleTimeout() {
        return delegate.getMaxIdleTimeout();
    }

    @Override
    public void setMaxIdleTimeout(long milliseconds) {
        delegate.setMaxIdleTimeout(milliseconds);
    }

    @Override
    public void setMaxBinaryMessageBufferSize(int length) {
        delegate.setMaxBinaryMessageBufferSize(length);
    }

    @Override
    public int getMaxBinaryMessageBufferSize() {
        return delegate.getMaxBinaryMessageBufferSize();
    }

    @Override
    public void setMaxTextMessageBufferSize(int length) {
        delegate.setMaxTextMessageBufferSize(length);
    }

    @Override
    public int getMaxTextMessageBufferSize() {
        return delegate.getMaxTextMessageBufferSize();
    }

    @Override
    public RemoteEndpoint.Async getAsyncRemote() {
        return delegate.getAsyncRemote();
    }

    @Override
    public RemoteEndpoint.Basic getBasicRemote() {
        return delegate.getBasicRemote();
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    @Override
    public void close(CloseReason closeReason) throws IOException {
        delegate.close(closeReason);
    }

    @Override
    public URI getRequestURI() {
        return delegate.getRequestURI();
    }

    @Override
    public Map<String, List<String>> getRequestParameterMap() {
        return delegate.getRequestParameterMap();
    }

    @Override
    public String getQueryString() {
        return delegate.getQueryString();
    }

    @Override
    public Map<String, String> getPathParameters() {
        return delegate.getPathParameters();
    }

    @Override
    public Map<String, Object> getUserProperties() {
        return delegate.getUserProperties();
    }

    @Override
    public Principal getUserPrincipal() {
        return delegate.getUserPrincipal();
    }

    @Override
    public Set<Session> getOpenSessions() {
        return delegate.getOpenSessions();
    }
}
