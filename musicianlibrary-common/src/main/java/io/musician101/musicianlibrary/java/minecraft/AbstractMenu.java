package io.musician101.musicianlibrary.java.minecraft;

import io.musician101.musicianlibrary.java.minecraft.AbstractMenu.ClickEventHandler;
import java.util.UUID;

public abstract class AbstractMenu<C, I, H extends ClickEventHandler, L, Q, S> {
    protected final H handler;
    protected final I inv;

    protected AbstractMenu(I inv, H handler) {
        this.inv = inv;
        this.handler = handler;
    }

    protected abstract void destroy();

    public abstract void onClick(L event);

    public abstract void onClose(C event);

    public abstract void onQuit(Q event);

    public abstract void open(UUID uuid);

    public abstract void setOption(int slot, S itemStack);

    public abstract void setOption(int slot, S itemStack, String name);

    public abstract void setOption(int slot, S itemStack, String name, String... description);

    public abstract void setOption(int slot, S itemStack, String name, boolean willGlow, String... description);

    @FunctionalInterface
    public interface ClickEventHandler<E extends ClickEvent> {
        void handle(E event);
    }

    public static class ClickEvent<S, P> {
        final S itemStack;
        final P player;
        final int slot;
        boolean close = false;
        boolean destroy = false;

        public ClickEvent(P player, S itemStack, int slot) {
            this.player = player;
            this.itemStack = itemStack;
            this.slot = slot;
        }

        public S getItem() {
            return itemStack;
        }

        public P getPlayer() {
            return player;
        }

        public int getSlot() {
            return slot;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }

        public boolean willClose() {
            return close;
        }

        public boolean willDestroy() {
            return destroy;
        }
    }
}
