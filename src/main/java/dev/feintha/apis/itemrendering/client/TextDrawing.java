package dev.feintha.apis.itemrenderingapi.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public class TextDrawing {
//    public static class Block {
//        public static Builder create() {
//            return new Builder();
//        }
//        public static class Builder {
//            protected int x = 0, y = 0, light = 0;
//            protected Text text = Text.empty();
//            protected float scale = 1, zOffset = 0, rotationAlongAxis = 0;
//            protected boolean flipX = false, flipY = false, shadow = true;
//            protected Direction side = Direction.UP;
//            private Builder(){};
//            public Builder x(int x) {this.x = x; return this;}
//            public Builder y(int y) {this.y = y; return this;}
//            public Builder flipX(boolean flip) {this.flipX = flip; return this;}
//            public Builder flipY(boolean flip) {this.flipY = flip; return this;}
//            public Builder shadow(boolean shadow) {this.shadow = shadow; return this;}
//            public Builder light(int light) {this.light = light; return this;}
//            public Builder text(Text text) {this.text = text; return this;}
//            public Builder text(String text) {this.text = Text.of(text); return this;}
//            public Builder side(Direction side) {this.side = side; return this;}
//            public Builder scale(float scale) {this.scale = scale; return this;}
//            public Builder zOffset(float zOffset) {this.zOffset = zOffset; return this;}
//            public Builder rotation(float rotationAlongAxis) {this.rotationAlongAxis = rotationAlongAxis; return this;}
//
//
//            public Builder(int x, int y, int light, Text text, float scale, float zOffset, float rotationAlongAxis, boolean flipX, boolean flipY, boolean shadow, Direction side) {
//                this.x = x;
//                this.y = y;
//                this.light = light;
//                this.text = text;
//                this.scale = scale;
//                this.zOffset = zOffset;
//                this.rotationAlongAxis = rotationAlongAxis;
//                this.flipX = flipX;
//                this.flipY = flipY;
//                this.shadow = shadow;
//                this.side = side;
//            }
//
//            public void draw(MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
//                drawText(x,y, text, scale, matrices, vertexConsumers, light, shadow, side, zOffset, rotationAlongAxis, flipX, flipY);
//            }
//
//        }
//        static void drawText(int x, int y, Text text, float scale, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean shadow, Direction side, float zOffset, float rotationAlongAxis, boolean flipHorizontal, boolean flipVertical) {
//            matrices.push();
//            side = side.getOpposite();
//
//            var vec = new Vec3d(side.getUnitVector()).offset(side, (zOffset - 24f) / 16f).offset(side, 0.0001f);
//            matrices.multiply(RotationAxis.POSITIVE_Y.rotation(rotationAlongAxis * MathHelper.RADIANS_PER_DEGREE));
//            matrices.multiply(side.getRotationQuaternion());
//            matrices.translate(0,-1.5,0.5);
//            matrices.translate(vec.x, vec.y, vec.z);
//            matrices.scale(1/16f,1/16f,-1/16f);
//            matrices.multiply(RotationAxis.POSITIVE_X.rotation(90 * MathHelper.RADIANS_PER_DEGREE));
//            matrices.scale(scale, scale, scale);
//            MinecraftClient.getInstance().textRenderer.draw(text, x,-y+8, 0xffffffff, shadow, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
//            matrices.pop();
//        }
//    }

    // Draws text. Assumes position at origin of item model! (test on glass panes to see easily)
    public static void drawText(int x, int y, String text, float scale, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float zOffset) {
        drawText(x, y, Text.of(text), scale, matrices,  vertexConsumers, light, true, zOffset);
    }
    // Draws text. Assumes position at origin of item model! (test on glass panes to see easily)
    public static void drawText(int x, int y, String text, float scale, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean shadow, float zOffset) {
        drawText(x, y, Text.of(text), scale, matrices,  vertexConsumers, light, shadow, zOffset);
    }
    // Draws text. Assumes position at origin of item model! (test on glass panes to see easily)
    public static void drawText(int x, int y, Text text, float scale, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float zOffset) {
        drawText(x,y, text, scale, matrices, vertexConsumers, light, true, zOffset);
    }

    // Draws text. Assumes position at origin of item model! (test on glass panes to see easily)
    public static void drawText(int x, int y, String text, float scale, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        drawText(x, y, Text.of(text), scale, matrices,  vertexConsumers, light, true, 0);
    }
    // Draws text. Assumes position at origin of item model! (test on glass panes to see easily)
    public static void drawText(int x, int y, String text, float scale, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean shadow) {
        drawText(x, y, Text.of(text), scale, matrices,  vertexConsumers, light, shadow, 0);
    }
    // Draws text. Assumes position at origin of item model! (test on glass panes to see easily)
    public static void drawText(int x, int y, Text text, float scale, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        drawText(x,y, text, scale, matrices, vertexConsumers, light, true, 0);
    }
    // Draws text. Assumes position at origin of item model! (test on glass panes to see easily)
    public static void drawText(int x, int y, Text text, float scale, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, boolean shadow, float zOffset) {
        matrices.push();
        matrices.translate(0,0,(zOffset + 8.55f)/16f);
        matrices.scale(1/16f,1/16f,-1/16f);
        matrices.scale(scale, scale, 1);
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(180 * MathHelper.RADIANS_PER_DEGREE));
        MinecraftClient.getInstance().textRenderer.draw(text, x,-y-7, 0xffffffff, shadow, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
        matrices.pop();
    }
}