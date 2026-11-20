package dev.coreclient.coreessentials;

import dev.coreclient.coreessentials.config.Config;
import dev.coreclient.coreessentials.hud.HudOverlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * CORECLIENT Essentials entrypoint.
 *
 * <p>Registers the HUD element and the toggle keybindings. Only stable
 * Fabric API surfaces are used so the mod builds against future 1.21.x
 * releases without mixin maintenance.</p>
 */
public final class CoreEssentialsClient implements ClientModInitializer {
    public static final String MOD_ID = "coreessentials";

    public static Config config;
    public static KeyBinding toggleHud;
    public static KeyBinding toggleKeystrokes;

    @Override
    public void onInitializeClient() {
        config = Config.load();

        toggleHud = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.coreessentials.toggle_hud", GLFW.GLFW_KEY_F4, KeyBinding.Category.MISC));
        toggleKeystrokes = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.coreessentials.toggle_keystrokes", GLFW.GLFW_KEY_F6, KeyBinding.Category.MISC));

        HudElementRegistry.addLast(Identifier.of(MOD_ID, "core_hud"), new HudOverlay());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleHud.wasPressed()) {
                config.hudEnabled = !config.hudEnabled;
                Config.save(config);
            }
            while (toggleKeystrokes.wasPressed()) {
                config.keystrokesEnabled = !config.keystrokesEnabled;
                Config.save(config);
            }
        });
    }
}
