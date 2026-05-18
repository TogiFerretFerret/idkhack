package sh.idktheflag.idk.impl.gui;

import sh.idktheflag.idk.api.gui.GUI;
import sh.idktheflag.idk.api.gui.context.Context;
import sh.idktheflag.idk.api.gui.helpers.MouseHelper;
import sh.idktheflag.idk.api.gui.helpers.Rect;
import sh.idktheflag.idk.api.management.accounts.Account;
import sh.idktheflag.idk.api.management.accounts.Accounts;
import sh.idktheflag.idk.api.management.accounts.MicrosoftLogin;
import sh.idktheflag.idk.api.management.accounts.types.CrackedAccount;
import sh.idktheflag.idk.api.management.accounts.types.MicrosoftAccount;
import sh.idktheflag.idk.api.gui.render.IRenderer;
import sh.idktheflag.idk.impl.gui.renderer.Renderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;

import java.awt.*;

public class AccountScreen extends GUI {
    private final Screen parent;
    private String status = "Idle";
    private String crackedName = "";
    private boolean typingCracked = false;

    public AccountScreen(Screen parent) {
        super(new Context(null, null, null, new Renderer(), null));
        this.parent = parent;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        getContext().setDrawContext(drawContext);
        drawContext.fill(0, 0, width, height, new Color(0, 0, 0, 150).getRGB());

        IRenderer r = getContext().getRenderer();
        MouseHelper mouse = new MouseHelper(mouseX, mouseY);

        int x = width / 2 - 100;
        int y = 50;

        r.renderText(drawContext, "Account Switcher", x, y - 20, Color.WHITE, true);
        r.renderText(drawContext, "Status: " + status, x, y - 10, Color.GRAY, true);

        // Accounts list
        if (Accounts.INSTANCE != null) {
            for (Account<?> account : Accounts.INSTANCE) {
                Rect rect = new Rect(x, y, 200, 20);
                Color bg = rect.collideWithMouse(mouse) ? new Color(60, 60, 60, 200) : new Color(40, 40, 40, 200);
                r.renderRect(rect, bg, bg, IRenderer.RectMode.Fill, getContext());
                r.renderText(drawContext, account.getUsername() + " (" + account.getType().name() + ")", x + 5, y + 5, Color.WHITE, true);
                y += 25;
            }
        }

        // Add Cracked
        Rect crackedRect = new Rect(x, y, 200, 20);
        Color crackedBg = crackedRect.collideWithMouse(mouse) ? new Color(60, 60, 60, 200) : new Color(40, 40, 40, 200);
        r.renderRect(crackedRect, crackedBg, crackedBg, IRenderer.RectMode.Fill, getContext());
        String display = typingCracked ? crackedName + "_" : (crackedName.isEmpty() ? "Enter cracked name..." : crackedName);
        r.renderText(drawContext, "Add Cracked: " + display, x + 5, y + 5, Color.LIGHT_GRAY, true);
        y += 25;

        // Add Microsoft
        Rect msRect = new Rect(x, y, 200, 20);
        Color msBg = msRect.collideWithMouse(mouse) ? new Color(60, 60, 60, 200) : new Color(40, 40, 40, 200);
        r.renderRect(msRect, msBg, msBg, IRenderer.RectMode.Fill, getContext());
        r.renderText(drawContext, "Add Microsoft Account", x + 5, y + 5, Color.CYAN, true);
        y += 25;

        // Back
        Rect backRect = new Rect(x, y, 200, 20);
        Color backBg = backRect.collideWithMouse(mouse) ? new Color(60, 60, 60, 200) : new Color(40, 40, 40, 200);
        r.renderRect(backRect, backBg, backBg, IRenderer.RectMode.Fill, getContext());
        r.renderText(drawContext, "Back", x + 5, y + 5, Color.RED, true);
    }

    @Override
    public boolean mouseClicked(Click click, boolean bl) {
        int mouseX = (int) click.x();
        int mouseY = (int) click.y();
        int button = click.button();
        MouseHelper mouse = new MouseHelper(mouseX, mouseY);
        int x = width / 2 - 100;
        int y = 50;

        if (Accounts.INSTANCE != null) {
            for (Account<?> account : Accounts.INSTANCE) {
                Rect rect = new Rect(x, y, 200, 20);
                if (rect.collideWithMouse(mouse)) {
                    if (button == 0) {
                        status = "Logging in to " + account.getUsername() + "...";
                        new Thread(() -> {
                            try {
                                if (account.login()) {
                                    status = "Logged in as " + account.getUsername();
                                } else {
                                    status = "Failed to login to " + account.getUsername();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                status = "Error: " + e.getMessage();
                            }
                        }).start();
                    } else if (button == 1) {
                        Accounts.INSTANCE.remove(account);
                        return true;
                    }
                    return true;
                }
                y += 25;
            }
        }

        Rect crackedRect = new Rect(x, y, 200, 20);
        if (crackedRect.collideWithMouse(mouse)) {
            typingCracked = true;
            return true;
        }
        y += 25;

        Rect msRect = new Rect(x, y, 200, 20);
        if (msRect.collideWithMouse(mouse)) {
            status = "Waiting for browser...";
            MicrosoftLogin.getRefreshToken(token -> {
                if (token != null) {
                    MicrosoftAccount acc = new MicrosoftAccount(token);
                    new Thread(() -> {
                        status = "Fetching info...";
                        if (acc.fetchInfo()) {
                            Accounts.INSTANCE.add(acc);
                            status = "Added " + acc.getUsername();
                        } else {
                            status = "Failed to fetch MS info";
                        }
                    }).start();
                } else {
                    status = "MS Login cancelled or failed";
                }
            });
            return true;
        }
        y += 25;

        Rect backRect = new Rect(x, y, 200, 20);
        if (backRect.collideWithMouse(mouse)) {
            mc.setScreen(parent);
            return true;
        }

        typingCracked = false;
        return super.mouseClicked(click, bl);
    }

    @Override
    public boolean keyPressed(KeyInput keyInput) {
        int keyCode = keyInput.key();
        if (typingCracked) {
            if (keyCode == 259) { // Backspace
                if (!crackedName.isEmpty()) crackedName = crackedName.substring(0, crackedName.length() - 1);
                return true;
            }
            if (keyCode == 257) { // Enter
                if (!crackedName.isEmpty()) {
                    CrackedAccount acc = new CrackedAccount(crackedName);
                    acc.fetchInfo();
                    Accounts.INSTANCE.add(acc);
                    crackedName = "";
                    typingCracked = false;
                }
                return true;
            }
        }
        return super.keyPressed(keyInput);
    }

    @Override
    public void onCharTyped(char character) {
        if (typingCracked) {
            if (character >= 32 && character <= 126) {
                crackedName += character;
            }
        }
        super.onCharTyped(character);
    }
}
