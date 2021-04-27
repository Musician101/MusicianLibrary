package io.musician101.musicianlibrary.java.minecraft.sponge.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import io.musician101.musicianlibrary.java.minecraft.sponge.event.AffectBookEvent.EditBookEvent;
import io.musician101.musicianlibrary.java.minecraft.sponge.event.AffectBookEvent.SignBookEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Cause;
import org.spongepowered.api.event.EventContext;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001)
public abstract class MixinNetHandlerPlayServer {

    @Shadow(remap = false)
    public net.minecraft.server.level.ServerPlayer player;

    private ItemStackSnapshot getSnapshot(ItemStack itemStack) {
        return org.spongepowered.api.item.inventory.ItemStack.class.cast(itemStack).createSnapshot();
    }

    @Inject(method = "updateBookContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;setTagInfo(Ljava/lang/String;Lnet/minecraft/nbt/INBT;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true, remap = false)
    public void onBookEdit(List<String> pages, int slot, CallbackInfo ci, ListTag pagesNBT, ItemStack itemStack) {
        ServerPlayer spongePlayer = (ServerPlayer) player;
        ItemStack original = itemStack.copy();
        original.addTagElement("pages", pagesNBT);
        Transaction<ItemStackSnapshot> transaction = new Transaction<>(getSnapshot(itemStack), getSnapshot(original));
        EditBookEvent event = new EditBookEvent(spongePlayer, transaction, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, spongePlayer).build(), player));
        Sponge.eventManager().post(event);
        if (!event.isCancelled()) {
            Transaction<ItemStackSnapshot> postEventTransaction = event.getTransaction();
            pagesNBT.clear();
            postEventTransaction.finalReplacement().createStack().get(Keys.PAGES).orElse(Collections.emptyList()).forEach(page -> pagesNBT.add(processJson(PlainComponentSerializer.plain().serialize(page))));
            itemStack.addTagElement("pages", pagesNBT);
            return;
        }

        updateInventory(slot, original);
        ci.cancel();
    }

    @Inject(method = "signBook", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true, remap = false)
    public void onBookSign(String title, List<String> pages, int slot, CallbackInfo ci, String channelName, ItemStack itemStack) {
        ItemStack original = itemStack.copy();
        ServerPlayer spongePlayer = (ServerPlayer) player;
        CompoundTag tag = itemStack.getTag();
        if (tag == null) {
            tag = new CompoundTag();
        }

        ListTag pagesNBT = tag.getList("pages", 9);
        List<Component> pagesText = new ArrayList<>();
        for (int i = 0; i < pagesNBT.size(); i++) {
            pagesText.add(Component.text(pagesNBT.getString(i)));
        }

        org.spongepowered.api.item.inventory.ItemStack writtenBook = org.spongepowered.api.item.inventory.ItemStack.builder().fromSnapshot(getSnapshot(itemStack)).itemType(ItemTypes.WRITTEN_BOOK).add(Keys.AUTHOR, Component.text(spongePlayer.name())).add(Keys.DISPLAY_NAME, Component.text(itemStack.getTag().getString("title"))).add(Keys.PAGES, pagesText).build();
        Transaction<ItemStackSnapshot> transaction = new Transaction<>(getSnapshot(original), writtenBook.createSnapshot());
        SignBookEvent event = new SignBookEvent(spongePlayer, transaction, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, spongePlayer).build(), player));
        Sponge.eventManager().post(event);
        if (!event.isCancelled()) {
            Transaction<ItemStackSnapshot> postEventTransaction = event.getTransaction();
            ListTag newPagesNBT = new ListTag();
            org.spongepowered.api.item.inventory.ItemStack finalStack = postEventTransaction.finalReplacement().createStack();
            finalStack.get(Keys.PAGES).orElse(Collections.emptyList()).forEach(page -> {
                String s = PlainComponentSerializer.plain().serialize(page);
                JsonElement initialJson = new Gson().fromJson(s, JsonObject.class).get("text");
                try {
                    newPagesNBT.add(processJson(initialJson.getAsString()));
                }
                catch (JsonParseException e) {
                    newPagesNBT.add(StringTag.valueOf(initialJson.getAsString()));
                }
            });

            original = ItemStack.class.cast(postEventTransaction.finalReplacement().createStack());
            original.addTagElement("pages", newPagesNBT);
            player.setItemInHand(InteractionHand.MAIN_HAND, original);
        }

        updateInventory(slot, original);
        ci.cancel();
    }

    private StringTag processJson(String json) throws JsonParseException {
        JsonObject jsonObject = new Gson().fromJson(json.replace("\\", ""), JsonObject.class);
        return StringTag.valueOf(jsonObject.get("text").getAsString());
    }

    private void updateInventory(int slot, ItemStack itemStack) {
        player.inventory.setItem(slot, itemStack);
    }
}
