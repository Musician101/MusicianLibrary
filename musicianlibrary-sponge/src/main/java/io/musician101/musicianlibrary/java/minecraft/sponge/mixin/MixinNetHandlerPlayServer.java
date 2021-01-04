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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraftforge.common.util.Constants.NBT;
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

/**
 * @deprecated nonfunctional atm due to mixins not working properly
 */
@Deprecated
@Mixin(value = ServerPlayNetHandler.class, priority = 1001)
public abstract class MixinNetHandlerPlayServer implements IServerPlayNetHandler {

    @Shadow
    public ServerPlayerEntity player;

    private ItemStackSnapshot getSnapshot(net.minecraft.item.ItemStack itemStack) {
        return org.spongepowered.api.item.inventory.ItemStack.class.cast(itemStack).createSnapshot();
    }

    @Inject(method = "func_244536_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setTagInfo(Ljava/lang/String;Lnet/minecraft/nbt/INBT;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void onBookEdit(List<String> pages, int slot, CallbackInfo ci, String channelName, PacketBuffer packetBuffer, net.minecraft.item.ItemStack itemStack, net.minecraft.item.ItemStack itemStack1) {
        ServerPlayer spongePlayer = (ServerPlayer) player;
        Transaction<ItemStackSnapshot> transaction = new Transaction<>(getSnapshot(itemStack1), getSnapshot(itemStack));
        EditBookEvent event = new EditBookEvent(spongePlayer, transaction, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, spongePlayer).build(), player));
        Sponge.getEventManager().post(event);
        if (!event.isCancelled()) {
            Transaction<ItemStackSnapshot> postEventTransaction = event.getTransaction();
            ListNBT pagesNBT = new ListNBT();
            postEventTransaction.getFinal().createStack().get(Keys.PAGES).orElse(Collections.emptyList()).forEach(page -> pagesNBT.add(processJson(PlainComponentSerializer.plain().serialize(page))));
            itemStack1.setTagInfo("pages", pagesNBT);
        }

        updateInventory(player, slot, itemStack1);
        ci.cancel();
    }

    @Inject(method = "func_244534_a", at = @At(value = "NEW", target = "net/minecraft/item/ItemStack", ordinal = 0), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
    public void onBookSign(String title, List<String> pages, int slot, CallbackInfo ci, String channelName, PacketBuffer packetBuffer, net.minecraft.item.ItemStack itemStack, net.minecraft.item.ItemStack itemStack1) {
        ServerPlayer spongePlayer = (ServerPlayer) player;
        CompoundNBT tag = itemStack.getTag();
        if (tag == null) {
            tag = new CompoundNBT();
        }

        ListNBT pagesNBT = tag.getList("pages", NBT.TAG_LIST);
        List<Component> pagesText = new ArrayList<>();
        for (int i = 0; i < pagesNBT.size(); i++) {
            pagesText.add(Component.text(pagesNBT.getString(i)));
        }

        org.spongepowered.api.item.inventory.ItemStack writtenBook = org.spongepowered.api.item.inventory.ItemStack.builder().fromSnapshot(getSnapshot(itemStack)).itemType(ItemTypes.WRITTEN_BOOK).add(Keys.AUTHOR, Component.text(spongePlayer.getName())).add(Keys.DISPLAY_NAME, Component.text(itemStack.getTag().getString("title"))).add(Keys.PAGES, pagesText).build();
        Transaction<ItemStackSnapshot> transaction = new Transaction<>(getSnapshot(itemStack1), writtenBook.createSnapshot());
        SignBookEvent event = new SignBookEvent(spongePlayer, transaction, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, spongePlayer).build(), player));
        Sponge.getEventManager().post(event);
        if (!event.isCancelled()) {
            Transaction<ItemStackSnapshot> postEventTransaction = event.getTransaction();
            ListNBT newPagesNBT = new ListNBT();
            org.spongepowered.api.item.inventory.ItemStack finalStack = postEventTransaction.getFinal().createStack();
            finalStack.get(Keys.PAGES).orElse(Collections.emptyList()).forEach(page -> {
                String s = PlainComponentSerializer.plain().serialize(page);
                JsonElement initialJson = new Gson().fromJson(s, JsonObject.class).get("text");
                try {
                    newPagesNBT.add(processJson(initialJson.getAsString()));
                }
                catch (JsonParseException e) {
                    newPagesNBT.add(StringNBT.valueOf(initialJson.getAsString()));
                }
            });

            itemStack1 = net.minecraft.item.ItemStack.class.cast(postEventTransaction.getFinal().createStack());
            itemStack1.setTagInfo("pages", newPagesNBT);
            player.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemStack1);
        }

        updateInventory(player, slot, itemStack1);
        ci.cancel();
    }

    private StringNBT processJson(String json) throws JsonParseException {
        JsonObject jsonObject = new Gson().fromJson(json.replace("\\", ""), JsonObject.class);
        return StringNBT.valueOf(jsonObject.get("text").getAsString());
    }

    private void updateInventory(ServerPlayerEntity player, int slot, net.minecraft.item.ItemStack itemStack) {
        player.inventory.setInventorySlotContents(slot, itemStack);
    }
}
