/**
 * Copyright (C) 2010 Lazaro Brito
 *
 * This file is part of RT5E.
 *
 * RT5E is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RT5E is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RT5E.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.lazaro.rt5e.logic.mask;

/**
 * @author Lazaro
 */
public class Masks {
    private Animation animation = null;
    private boolean appearance = false;
    private ChatMessage chat = null;
    private FaceDirection faceDirection = null;
    private ChatMessage forcedChat = null;
    private Graphics graphics;
    private Hit hit, hit2;
    private boolean resetFaceDirection;

    public Animation getAnimation() {
        return animation;
    }

    public ChatMessage getChat() {
        return chat;
    }

    public FaceDirection getFaceDirection() {
        return faceDirection;
    }

    public ChatMessage getForcedChat() {
        return forcedChat;
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public Hit getHit() {
        return hit;
    }

    public Hit getHit2() {
        return hit2;
    }

    public boolean isAppearance() {
        return appearance;
    }

    public boolean requiresUpdate() {
        return animation != null || appearance || chat != null
                || forcedChat != null || faceDirection != null
                || graphics != null || hit != null || hit2 != null;
    }

    public boolean requiresUpdate(Mask.MaskType mask) {
        switch (mask) {
            case ANIMATION:
                return animation != null;
            case APPEARANCE:
                return appearance;
            case CHAT:
                return chat != null;
            case FORCED_CHAT:
                return forcedChat != null;
            case FACE_DIRECTION:
                return faceDirection != null;
            case GRAPHICS:
                return graphics != null;
            case HIT:
                return hit != null;
            case HIT2:
                return hit2 != null;
        }
        return false;
    }

    public void reset() {
        animation = null;
        appearance = false;
        chat = null;
        forcedChat = null;
        faceDirection = null;
        graphics = null;
        hit = null;
        hit2 = null;
        if (resetFaceDirection) {
            faceDirection = new FaceDirection(-1);
            resetFaceDirection = false;
        }
    }

    public void resetFaceDirection() {
        faceDirection = new FaceDirection(-1);
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public void setAppearance(boolean appearance) {
        this.appearance = appearance;
    }

    public void setChat(ChatMessage chat) {
        this.chat = chat;
    }

    public void setFaceDirection(FaceDirection faceDirection) {
        this.faceDirection = faceDirection;
    }

    public void setForcedChat(ChatMessage forcedChat) {
        this.forcedChat = forcedChat;
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
    }

    public void setHit2(Hit hit2) {
        this.hit2 = hit2;
    }
}
