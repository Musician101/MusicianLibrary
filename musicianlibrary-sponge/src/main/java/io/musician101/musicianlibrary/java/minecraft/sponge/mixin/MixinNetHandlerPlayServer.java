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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketSetSlot;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = NetHandlerPlayServer.class, priority = 1001)
public abstract class MixinNetHandlerPlayServer implements INetHandlerPlayServer {

    @Shadow public EntityPlayerMP player;

    @Shadow public abstract void sendPacket(Packet<?> packetIn);

    //TODO need to use TextSerializers.PLAIN for easier freakin easier translating x-x
    @Inject(method = "processCustomPayload", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setTagInfo(Ljava/lang/String;Lnet/minecraft/nbt/NBTBase;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void onBookEdit(CPacketCustomPayload packetIn, CallbackInfo ci, String channelName, PacketBuffer packetBuffer, net.minecraft.item.ItemStack itemStack, net.minecraft.item.ItemStack itemStack1) {
        Player spongePlayer = (Player) player;
        Transaction<ItemStackSnapshot> transaction = new Transaction<>(getSnapshot(itemStack1), getSnapshot(itemStack));
        EditBookEvent event = new EditBookEvent(spongePlayer, transaction, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, spongePlayer).build(), player));
        Sponge.getEventManager().post(event);
        if (!event.isCancelled()) {
            Transaction<ItemStackSnapshot> postEventTransaction = event.getTransaction();
            NBTTagList pages = new NBTTagList();
            postEventTransaction.getFinal().createStack().get(Keys.BOOK_PAGES).orElse(Collections.emptyList()).forEach(page -> pages.appendTag(processJson(page.toPlain())));
            itemStack1.setTagInfo("pages", pages);
        }

        updateInventory(player, itemStack1);
        ci.cancel();
    }

    @Inject(method = "processCustomPayload", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void onBookSign(CPacketCustomPayload packetIn, CallbackInfo ci, String channelName, PacketBuffer packetBuffer, net.minecraft.item.ItemStack itemStack, net.minecraft.item.ItemStack itemStack1) {
        Player spongePlayer = (Player) player;
        NBTTagList pagesNBT = itemStack.getTagCompound().getTagList("pages", 8);
        List<Text> pagesText = new ArrayList<>();
        for(int i = 0; i < pagesNBT.tagCount(); i++) {
            pagesText.add(Text.of(pagesNBT.getStringTagAt(i)));
        }
        org.spongepowered.api.item.inventory.ItemStack writtenBook = org.spongepowered.api.item.inventory.ItemStack.builder().fromSnapshot(getSnapshot(itemStack)).itemType(ItemTypes.WRITTEN_BOOK).add(Keys.BOOK_AUTHOR, Text.of(player.getName())).add(Keys.DISPLAY_NAME, Text.of(itemStack.getTagCompound().getString("title"))).add(Keys.BOOK_PAGES, pagesText).build();
        Transaction<ItemStackSnapshot> transaction = new Transaction<>(getSnapshot(itemStack1), writtenBook.createSnapshot());
        SignBookEvent event = new SignBookEvent(spongePlayer, transaction, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, spongePlayer).build(), player));
        Sponge.getEventManager().post(event);
        if (!event.isCancelled()) {
            Transaction<ItemStackSnapshot> postEventTransaction = event.getTransaction();
            NBTTagList pages = new NBTTagList();
            org.spongepowered.api.item.inventory.ItemStack finalStack = postEventTransaction.getFinal().createStack();
            finalStack.get(Keys.BOOK_PAGES).orElse(Collections.emptyList()).forEach(page -> {
                String s = page.toPlain();
                JsonElement initialJson = new Gson().fromJson(s, JsonObject.class).get("text");
                try {
                    pages.appendTag(processJson(initialJson.getAsString()));
                }
                catch (JsonParseException e) {
                    pages.appendTag(new NBTTagString(initialJson.getAsString()));
                }
            });

            itemStack1 = net.minecraft.item.ItemStack.class.cast(postEventTransaction.getFinal().createStack());
            itemStack1.setTagInfo("pages", pages);
            player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemStack1);
        }

        updateInventory(player, itemStack1);
        ci.cancel();
    }

    private NBTTagString processJson(String json) throws JsonParseException {
        JsonObject jsonObject = new Gson().fromJson(json.replace("\\", ""), JsonObject.class);
        return new NBTTagString(jsonObject.get("text").getAsString());
    }

    private void updateInventory(EntityPlayerMP player, net.minecraft.item.ItemStack itemStack) {
        Slot slot = player.openContainer.getSlotFromInventory(player.inventory, player.inventory.currentItem);
        sendPacket(new SPacketSetSlot(player.openContainer.windowId, slot.slotNumber, itemStack));
    }

    private ItemStackSnapshot getSnapshot(net.minecraft.item.ItemStack itemStack) {
        return org.spongepowered.api.item.inventory.ItemStack.class.cast(itemStack).createSnapshot();
    }
}
