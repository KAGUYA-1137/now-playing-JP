package com.github.scotsguy.nowplaying.sound;

import com.github.scotsguy.nowplaying.config.Config;
import com.github.scotsguy.nowplaying.gui.toast.NowPlayingToast;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEventListener;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import org.jetbrains.annotations.NotNull;

public class NowPlayingListener implements SoundEventListener {
    @Override
    public void onPlaySound(SoundInstance sound, @NotNull WeighedSoundEvents soundSet, float f) {
        Config config = Config.get();
        Minecraft minecraft = Minecraft.getInstance();
        Component name = Sound.getSoundName(sound);

        if (sound.getSource() == SoundSource.MUSIC) {
            Component message = Component.translatable("record.nowPlaying", name);

            if (config.options.musicStyle == Config.Options.Style.Toast) {
                minecraft.getToasts().addToast(new NowPlayingToast(name));
            }
            else if (config.options.musicStyle == Config.Options.Style.Hotbar) {
                Minecraft.getInstance().gui.setOverlayMessage(message, true);
            }

            if (config.options.narrate) {
                minecraft.getNarrator().sayNow(message);
            }
        }
        else if (sound.getSource() == SoundSource.RECORDS) {
            if (config.options.jukeboxStyle != Config.Options.Style.Toast) return;

            RecordItem disc = Sound.getDiscFromSound(sound);
            if (disc == null) return;

            minecraft.getToasts().addToast(new NowPlayingToast(disc.getDisplayName(), new ItemStack(disc)));

            if (config.options.narrate) {
                Component message = Component.translatable("record.nowPlaying", disc.getDisplayName());
                minecraft.getNarrator().sayNow(message);
            }
        }
    }
}
