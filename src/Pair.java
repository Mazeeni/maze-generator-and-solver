public class Pair {
  private final int x;
  private final int y;

  public Pair(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Pair) {
      return (this.x == ((Pair) obj).x) && (this.y == ((Pair) obj).y);
    }
    return false;
  }
}
