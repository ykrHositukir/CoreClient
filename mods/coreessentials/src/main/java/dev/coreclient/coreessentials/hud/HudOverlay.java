package dev.coreclient.coreessentials.hud;

import dev.coreclient.coreessentials.CoreEssentialsClient;
import dev.coreclient.coreessentials.config.Config;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.BlockPos;

/**
 * The CORECLIENT overlay: FPS, coordinates, facing, world clock, session
 * timer and an optional keystrokes panel.
 *
 * <p>Every Minecraft API touched here was verified against the 1.21.11 yarn
 * mappings: {@link DrawContext#fill}, {@link DrawContext#drawTextWithShadow},
 * {@link MinecraftClient#textRenderer}, {@code player.getBlockPos()},
 * {@code player.getYaw()}, {@code world.getTimeOfDay()} and
 * {@link KeyBinding#isPressed}.</p>
 */
public final class HudOverlay implements HudElement {
    private static final int PANEL_BG = 0x99050912;
    private static final int PANEL_LINE = 0xFF4FD8EB;
    private static final int FG = 0xFFE9F2FC;
    private static final int FG_DIM = 0xFF8DA0B6;
    private static final int KEY_BG = 0x9916202D;
    private static final int KEY_ACTIVE = 0xFF4FD8EB;
    private static final int KEY_FG_ACTIVE = 0xFF0B111A;

    private static final int CELL = 20;
    private static final int GAP = 2;

    private long frames;
    private long windowStart;
    private int fps;
    private final long sessionStart = System.currentTimeMillis();

    @Override
    public void render(DrawContext ctx, RenderTickCounter tickCounter) {
        Config config = CoreEssentialsClient.config;
        if (config == null || !config.hudEnabled) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) {
            return;
        }

        long now = System.currentTimeMillis();
        if (windowStart == 0) {
            windowStart = now;
        }
        frames++;
        if (now - windowStart >= 1000) {
            fps = (int) frames;
            frames = 0;
            windowStart = now;
        }

        int x = config.hudX;
        int y = config.hudY;
        int width = 138;
        int height = 66;

        panel(ctx, x, y, width, height);
        ctx.drawTextWithShadow(client.textRenderer, "CORECLIENT", x + 6, y + 4, PANEL_LINE);

        ctx.drawTextWithShadow(client.textRenderer, "FPS " + fps, x + 6, y + 17, FG);
        BlockPos pos = client.player.getBlockPos();
        ctx.drawTextWithShadow(client.textRenderer,
                "XYZ " + pos.getX() + " " + pos.getY() + " " + pos.getZ(), x + 6, y + 29, FG);
        ctx.drawTextWithShadow(client.textRenderer,
                "DIR " + facing(client.player.getYaw()), x + 6, y + 41, FG);
        long seconds = (now - sessionStart) / 1000;
        ctx.drawTextWithShadow(client.textRenderer,
                clock(client.world.getTimeOfDay()) + "  " + formatDuration(seconds), x + 6, y + 53, FG_DIM);

        if (config.keystrokesEnabled) {
            drawKeystrokes(ctx, client, x, y + height + 6);
        }
    }

    private void panel(DrawContext ctx, int x, int y, int width, int height) {
        ctx.fill(x, y, x + width, y + height, PANEL_BG);
        ctx.fill(x, y + height - 1, x + width, y + height, PANEL_LINE);
    }

    private void key(DrawContext ctx, MinecraftClient client, KeyBinding binding,
                     String label, int x, int y) {
        boolean pressed = binding.isPressed();
        ctx.fill(x, y, x + CELL, y + CELL, pressed ? KEY_ACTIVE : KEY_BG);
        int color = pressed ? KEY_FG_ACTIVE : FG;
        int w = client.textRenderer.getWidth(label);
        ctx.drawTextWithShadow(client.textRenderer, label, x + (CELL - w) / 2, y + 6, color);
    }

    private void drawKeystrokes(DrawContext ctx, MinecraftClient client, int x, int y) {
        // WASD cluster
        key(ctx, client, client.options.forwardKey, "W", x + CELL + GAP, y);
        key(ctx, client, client.options.leftKey, "A", x, y + CELL + GAP);
        key(ctx, client, client.options.backKey, "S", x + CELL + GAP, y + CELL + GAP);
        key(ctx, client, client.options.rightKey, "D", x + 2 * (CELL + GAP), y + CELL + GAP);
        // mouse buttons under the cluster
        key(ctx, client, client.options.attackKey, "LMB", x, y + 2 * (CELL + GAP));
        key(ctx, client, client.options.sprintKey, "SPR", x + CELL + GAP, y + 2 * (CELL + GAP));
        key(ctx, client, client.options.jumpKey, "JMP", x + 2 * (CELL + GAP), y + 2 * (CELL + GAP));
    }

    private static String facing(float yaw) {
        String[] dirs = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
        float normalized = ((yaw % 360f) + 360f) % 360f;
        int index = Math.round(normalized / 45f) % 8;
        return dirs[index];
    }

    private static String clock(long timeOfDay) {
        long ticks = (timeOfDay + 6000L) % 24000L;
        long hours = ticks / 1000L;
        long minutes = (ticks % 1000L) * 60L / 1000L;
        return String.format("%02d:%02d", hours, minutes);
    }

    private static String formatDuration(long seconds) {
        long h = seconds / 3600L;
        long m = (seconds % 3600L) / 60L;
        long s = seconds % 60L;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
