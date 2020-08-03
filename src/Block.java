public class Block {
  private final int x;
  private final int y;
  private boolean isSet;

  public Block(int x, int y) {
    this.x = x;
    this.y = y;
    set();
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isSet() {
    return isSet;
  }

  public void set() {
    isSet = true;
  }

  public void unSet() {
    isSet = false;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Block) {
      return (this.x == ((Block) obj).x) && (this.y == ((Block) obj).y);
    }
    return false;
  }
}
