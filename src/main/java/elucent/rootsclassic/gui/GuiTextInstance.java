package elucent.rootsclassic.gui;

import elucent.rootsclassic.Util;

public class GuiTextInstance {

  private String line;
  private float x;
  private float y;
  private int color;
  private boolean shadow = true;

  public GuiTextInstance(String line, float x, float y, int color, boolean shadow) {
    this.setLine(line);
    this.setX(x);
    this.setY(y);
    this.setColor(color);
    this.setShadow(shadow);
  }

  public GuiTextInstance(String line, float x, float y, int color) {
    this(line, x, y, color, true);
  }

  public GuiTextInstance(String line, float x, float y) {
    this(line, x, y, Util.intColor(255, 255, 255));
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }

  public boolean isShadow() {
    return shadow;
  }

  public void setShadow(boolean shadow) {
    this.shadow = shadow;
  }
}
