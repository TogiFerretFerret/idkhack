package me.skitttyy.kami.api.utils;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class MatrixHelper {
    public static MatrixStack fromContext(DrawContext context) {
        MatrixStack stack = new MatrixStack();
        // Copy the 2D translation from Matrix3x2fStack to MatrixStack
        var m = context.getMatrices();
        stack.translate(m.m20(), m.m21(), 0);
        return stack;
    }
}
